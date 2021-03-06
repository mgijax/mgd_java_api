package org.jax.mgi.mgd.api.model.bib.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowTag;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateParser;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {

	protected LTReferenceDAO() {
		super(LTReference.class);
	}

	// maps from search field name to workflow group abbreviation
	private static Map<String,String> groups = null;

	// maps from search field name to workflow group status
	private static Map<String,String> statuses = null;

	// maps from search field name to database key name (for created-by / modified-by fields)
	private static Map<String,String> users = null;

	// maps from search field name to creatition/modification date database field name
	private static Map<String,String> dates = null;

	private SQLExecutor sqlExecutor = new SQLExecutor();

	/* convenience method for instantiating a new search results object, populating its error fields,
	 * and returning it.
	 */
	private static SearchResults<LTReference> errorSR(String error, String message, int status_code) {
		SearchResults<LTReference> results = new SearchResults<LTReference>();
		results.setError(error, message, status_code);
		return results;
	}

	/* query handling specific for references.  Some fields are within the table backing Reference,
	 * while others are coming from related tables.
	 */
	@Override
	@Transactional
	public SearchResults<LTReference> search(Map<String, Object> params) {
		// query parameters existing in main reference table
		List<String> internalParameters = new ArrayList<String>(Arrays.asList(
			new String[] { "issue", "pgs", "date", "referenceAbstract", "isReviewArticle", "title",
				"authors", "primary_author", "journal", "vol", "_refs_key" }));

		// status query parameters (residing outside main reference table)
		List<String> statusParameters = new ArrayList<String>();

		// if caches are not yet built, fill the mappings as we check status parameters
		boolean populateCaches = false;
		if ((groups == null) || (statuses == null)) {
			populateCaches = true;
			groups = new HashMap<String,String>();
			statuses = new HashMap<String,String>();
		}

		for (String group : new String[] { "AP", "GO", "GXD", "QTL", "Tumor" }) {
			for (String status : new String[] { "Not_Routed", "Routed", "Indexed", "Chosen", "Full_coded", "Rejected"}) {
				String fieldname = "status_" + group + "_" + status;
				statusParameters.add(fieldname);

				if (populateCaches) {
					groups.put(fieldname, group);
					if ("Full_coded".equals(status)) {
						statuses.put(fieldname, status.replace("_", "-"));
					} else {
						statuses.put(fieldname, status.replace("_", " "));
					}
				}
			}
		}

		if (users == null) {
			users = new HashMap<String,String>();
			users.put("created_by", "createdByUser");
			users.put("modified_by", "modifiedByUser");
		}

		if (dates == null) {
			dates = new HashMap<String,String>();
			dates.put("creation_date", "Creation Date");
			dates.put("modification_date", "Modification Date");
		}

		// begin building the query

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LTReference> query = builder.createQuery(myClass);
		Root<LTReference> root = query.from(myClass);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		// first, handle the list of internal parameters (those that are in the table underlying Reference objects)
		for (String key: internalParameters) {
			Path<String> column = root.get(key);
			if (params.containsKey(key)) {
				Object desiredValue = params.get(key);

				// special handling for strings, depending on whether string contains a wildcard or any letters
				if (desiredValue instanceof String) {
					String valueString = (String) desiredValue;
					// has at least one wildcard, so do case-insensitive 'like' search
					if (valueString.indexOf("%") >= 0) {
						restrictions.add(builder.like(builder.lower(column), valueString.toLowerCase()));

					} else if (valueString.matches(".*[A-Za-z].*")) {
						// no wildcards, but has at least one letter, so we know this needs to be a
						// case-insensitive equals search
						restrictions.add(builder.equal(builder.lower(column), valueString.toLowerCase()));

					} else {
						// no wildcards, no letters -- do a simple equals search
						restrictions.add(builder.equal(root.get(key), desiredValue));
					}
				}
				else {
					// not a string, so just do an equals search
					restrictions.add(builder.equal(root.get(key), desiredValue));
				}
			}
		}

		// special internal parameter -- isDiscard.  QF specifies three values, which need to be translated
		// for the actual data in the bib_refs table.

		if (params.containsKey("isDiscard")) {
			String desiredValue = ((String) params.get("isDiscard")).toLowerCase();

			if (desiredValue.equals("no")) {
				restrictions.add(builder.equal(root.get("isDiscard"), 0));

			} else if (desiredValue.equals("only discard")) {
				restrictions.add(builder.equal(root.get("isDiscard"), 1));

			} else if (desiredValue.equals("search all")) {
				// disregard the isDiscard flag when searching
			}

		} else if (!params.containsKey("_refs_key")){
			// default setting is to only return non-discarded references -- only apply if we're not
			// doing a key-based lookup, though.
			restrictions.add(builder.equal(root.get("isDiscard"), 0));
		}

		// second, handle list of status parameters.  The status fields are always OR-ed within a group.
		// The status_operator field tells us whether to OR or AND them across groups (and defaults to OR).

		List<Predicate> wfsRestrictions = new ArrayList<Predicate>();

		boolean statusOperatorOR = true;			// false if operator across groups should be AND
		if (params.containsKey("status_operator") && (((String) params.get("status_operator")).trim().equalsIgnoreCase("AND"))) {
			statusOperatorOR = false;
		}

		// collect the desired statuses for each group

		Map<String,List<String>> statusByGroup = new HashMap<String,List<String>>();

		for (String key: statusParameters) {
			if (params.containsKey(key)) {
				String groupAbbrev = groups.get(key);
				String status = statuses.get(key);

				if (!statusByGroup.containsKey(groupAbbrev)) {
					statusByGroup.put(groupAbbrev, new ArrayList<String>());
				}
				statusByGroup.get(groupAbbrev).add(status);
			}
		}

		// compose one Exists subquery for each group

		for (String groupAbbrev : statusByGroup.keySet()) {
			Subquery<LTReferenceWorkflowStatus> wfsSubquery = query.subquery(LTReferenceWorkflowStatus.class);
			Root<LTReferenceWorkflowStatus> wfsRoot = wfsSubquery.from(LTReferenceWorkflowStatus.class);
			wfsSubquery.select(wfsRoot);

			List<Predicate> wfsPredicates = new ArrayList<Predicate>();
			wfsPredicates.add(builder.equal(root.get("_refs_key"), wfsRoot.get("_refs_key")));
			wfsPredicates.add(builder.equal(wfsRoot.get("isCurrent"), 1));
			wfsPredicates.add(builder.equal(wfsRoot.get("groupTerm").get("abbreviation"), groupAbbrev));
			Path<String> column = wfsRoot.get("statusTerm").get("term");
			wfsPredicates.add(column.in(statusByGroup.get(groupAbbrev)));

			wfsSubquery.where(wfsPredicates.toArray(new Predicate[]{}));
			wfsRestrictions.add(builder.exists(wfsSubquery));
		}

		// then either AND or OR the subqueries for the separate groups together

		if (wfsRestrictions.size() > 0) {
			if (statusOperatorOR) {
				restrictions.add(builder.or(wfsRestrictions.toArray(new Predicate[0])));
			} else {
				restrictions.add(builder.and(wfsRestrictions.toArray(new Predicate[0])));
			}
		}

		// special handling for 'year' allowing for fairly sophistaicated queries

		if (params.containsKey("year")) {
			Expression<Integer> yearField = root.get("year");
			try {
				restrictions.add(yearPredicate(builder, yearField, params.get("year").toString()));
			} catch (FatalAPIException e) {
				return errorSR("InvalidYearFormat", "Value for year field is invalid: " + e.toString(), Constants.HTTP_BAD_REQUEST);
			}
		}

		// created_by and modified_by parameters

		for (String fieldname : users.keySet()) {
			if (params.containsKey(fieldname)) {
				Subquery<User> userSubquery = query.subquery(User.class);
				Root<User> userRoot = userSubquery.from(User.class);
				userSubquery.select(userRoot);

				List<Predicate> userPredicates = new ArrayList<Predicate>();
				userPredicates.add(builder.equal(root.get(users.get(fieldname)), userRoot.get("_user_key")));
				Path<String> column = userRoot.get("login");
				Expression<String> lowerColumn = builder.lower(column);
				userPredicates.add(builder.like(lowerColumn, params.get(fieldname).toString().toLowerCase()));
				userSubquery.where(userPredicates.toArray(new Predicate[]{}));

				restrictions.add(builder.exists(userSubquery));
			}
		}

		// creation_date and modification_date parameters

		for (String fieldname : dates.keySet()) {
			if (params.containsKey(fieldname)) {
				try {
					DateParser parser = new DateParser();
					List<String> datePieces = parser.parse((String) params.get(fieldname));
					if (datePieces.size() >= 2) {
						Path<Date> path = root.get(fieldname);
						restrictions.add(datePredicate(builder, path, datePieces.get(0), datePieces.get(1)));
					}
					if (datePieces.size() >= 4) {
						Path<Date> path = root.get(fieldname);
						restrictions.add(datePredicate(builder, path, datePieces.get(2), datePieces.get(3)));
					}
				} catch (FatalAPIException e) {
					return errorSR("InvalidDateFormat", dates.get(fieldname) + " is invalid", Constants.HTTP_BAD_REQUEST);
				}
			}
		}

		// status history parameters (must find a single record that matches any of the specified history criteria)

		if ((params.containsKey("sh_group") || params.containsKey("sh_username") || params.containsKey("sh_status") || params.containsKey("sh_date"))) {
			String shGroup = (String) params.get("sh_group");
			String shUsername = (String) params.get("sh_username");
			String shStatus = (String) params.get("sh_status");
			String shDate = (String) params.get("sh_date");

			Subquery<LTReferenceWorkflowStatus> shSubquery = query.subquery(LTReferenceWorkflowStatus.class);
			Root<LTReferenceWorkflowStatus> shRoot = shSubquery.from(LTReferenceWorkflowStatus.class);
			shSubquery.select(shRoot);

			List<Predicate> shPredicates = new ArrayList<Predicate>();
			shPredicates.add(builder.equal(root.get("_refs_key"), shRoot.get("_refs_key")));
			if (shGroup != null) {
				Path<String> column = shRoot.get("groupTerm").get("abbreviation");
				Expression<String> lowerColumn = builder.lower(column);
				shPredicates.add(builder.equal(lowerColumn, shGroup.toLowerCase()));
			}
			if (shStatus != null) {
				Path<String> column = shRoot.get("statusTerm").get("term");
				Expression<String> lowerColumn = builder.lower(column);
				shPredicates.add(builder.equal(lowerColumn, shStatus.toLowerCase()));
			}
			if (shUsername != null) {
				Path<String> column = shRoot.get("modifiedByUser").get("login");
				Expression<String> lowerColumn = builder.lower(column);
				shPredicates.add(builder.like(lowerColumn, shUsername.toLowerCase()));
			}
			if (shDate != null) {
				DateParser parser = new DateParser();
				try {
					// datePieces will either have [ start operator, start date ]
					// or [ start operator, start date, end operator, end date ]
					List<String> datePieces = parser.parse(shDate);
					if (datePieces.size() >= 2) {
						Path<Date> path = shRoot.get("creation_date");
						shPredicates.add(datePredicate(builder, path, datePieces.get(0), datePieces.get(1)));
					}
					if (datePieces.size() == 4) {
						Path<Date> path = shRoot.get("creation_date");
						shPredicates.add(datePredicate(builder, path, datePieces.get(2), datePieces.get(3)));
					}
				} catch (APIException e) {
					return errorSR("InvalidDateFormat", "Status History Date is invalid", Constants.HTTP_BAD_REQUEST);
				}
			}

			if (shPredicates.size() > 0) {
				shSubquery.where(shPredicates.toArray(new Predicate[]{}));
				restrictions.add(builder.exists(shSubquery));
			}
		}

		// book fields

		if ( (params.containsKey("book_author")) || (params.containsKey("book_title")) || (params.containsKey("place")) || (params.containsKey("publisher")) || (params.containsKey("series_ed"))) {

			String bookAuthor  = (String) params.get("book_author");
			String booktitle   = (String) params.get("book_title");
			String place       = (String) params.get("place");
			String publisher   = (String) params.get("publisher");
			String seriesEd    = (String) params.get("series_ed");

			Subquery<ReferenceBook> bookSubquery = query.subquery(ReferenceBook.class);
			Root<ReferenceBook> bookRoot = bookSubquery.from(ReferenceBook.class);			
			bookSubquery.select(bookRoot);

			List<Predicate> bookPredicates = new ArrayList<Predicate>();
			bookPredicates.add(builder.equal(root.get("_refs_key"), bookRoot.get("_refs_key")));


			if (bookAuthor != null) {
				Path<String> column = bookRoot.get("book_author");
				Expression<String> lowerColumn = builder.lower(column);
				bookPredicates.add(builder.like(lowerColumn, bookAuthor.toLowerCase()));
			}
			if (booktitle != null) {
				Path<String> column = bookRoot.get("book_title");
				Expression<String> lowerColumn = builder.lower(column);
				bookPredicates.add(builder.like(lowerColumn, booktitle.toLowerCase()));
			}
			if (place != null) {
				Path<String> column = bookRoot.get("place");
				Expression<String> lowerColumn = builder.lower(column);
				bookPredicates.add(builder.like(lowerColumn, place.toLowerCase()));
			}
			if (publisher != null) {
				Path<String> column = bookRoot.get("publisher");
				Expression<String> lowerColumn = builder.lower(column);
				bookPredicates.add(builder.like(lowerColumn, publisher.toLowerCase()));
			}
			if (seriesEd != null) {
				Path<String> column = bookRoot.get("series_ed");
				Expression<String> lowerColumn = builder.lower(column);
				bookPredicates.add(builder.like(lowerColumn, seriesEd.toLowerCase()));
			}

			if (bookPredicates.size() > 0) {
				bookSubquery.where(bookPredicates.toArray(new Predicate[]{}));
				restrictions.add(builder.exists(bookSubquery));
			}
		}


		// third handle list of external parameters, including:
		//		"notes", "referenceType", "marker_id", "allele_id", "accids", "workflow_tag", "supplementalTerm"

		if (params.containsKey("notes")) {
			Subquery<ReferenceNote> noteSubquery = query.subquery(ReferenceNote.class);
			Root<ReferenceNote> noteRoot = noteSubquery.from(ReferenceNote.class);
			noteSubquery.select(noteRoot);

			List<Predicate> notePredicates = new ArrayList<Predicate>();
			notePredicates.add(builder.equal(root.get("_refs_key"), noteRoot.get("_refs_key")));
			Path<String> column = noteRoot.get("note");
			notePredicates.add(builder.like(builder.lower(column), ((String) params.get("notes")).toLowerCase()));

			noteSubquery.where(notePredicates.toArray(new Predicate[]{}));
			restrictions.add(builder.exists(noteSubquery));
		}

		if (params.containsKey("referenceType")) {
			restrictions.add(builder.equal(root.get("referenceTypeTerm").get("term"), params.get("referenceType")));
		}

		if (params.containsKey("accids")) {
			String idString = (String) params.get("accids");
			String[] accids = idString.toLowerCase().replaceAll(",", " ").replaceAll(" +", " ").split(" ");

			List<Predicate> idRestrictions = new ArrayList<Predicate>();

			Subquery<Accession> idSubquery = query.subquery(Accession.class);
			Root<Accession> idRoot = idSubquery.from(Accession.class);
			idSubquery.select(idRoot);

			List<Predicate> idPredicates = new ArrayList<Predicate>();

			idPredicates.add(builder.equal(root.get("_refs_key"), idRoot.get("_object_key")));
			Path<String> column = idRoot.get("accID");
			idPredicates.add(builder.lower(column).in((Object[]) accids));
			idPredicates.add(builder.equal(idRoot.get("mgiType").get("_mgitype_key"), 1));

			idSubquery.where(idPredicates.toArray(new Predicate[]{}));
			idRestrictions.add(builder.exists(idSubquery));

			if (idRestrictions.size() > 0) {
				restrictions.add(builder.or(idRestrictions.toArray(new Predicate[0])));
			}
		}

		// Deal with workflow tag searching.  This involves five tag fields paired with five NOT operator
		// fields, plus an operator field that specifies how they should all be combined together.  Coded
		// to not matter if user fills in tags in order starting with field #1.  And, we ignore a "NOT"
		// checkbox if the corresponding tag field is not filled in.

		List<String> tags = new ArrayList<String>();	// tags specified by user as positive searches (up to 5)
		List<String> notTags = new ArrayList<String>();	// tags specified by user as NOT searches (up to 5)

		for (int i = 1; i <= 5; i++) {
			String tagField = "workflow_tag" + i;
			String notField = "not_" + tagField;

			if (params.containsKey(tagField)) {
				if (params.containsKey(notField)) {
					String notParam = params.get(notField).toString().trim();
					if ("true".equalsIgnoreCase(notParam) || "1".equals(notParam)) {
						notTags.add(((String) params.get(tagField)).toLowerCase());
					} else {
						tags.add(((String) params.get(tagField)).toLowerCase());
					}
				} else {
					tags.add(((String) params.get(tagField)).toLowerCase());
				}
			}
		}

		if ((tags.size() > 0) || (notTags.size() > 0)) {
			// Default state is to join multiple tags in a search with AND, but if the user specified OR
			// then we can do that instead.

			boolean useAnd = true;
			if (params.containsKey("workflow_tag_operator")) {
				if (((String) params.get("workflow_tag_operator")).trim().equalsIgnoreCase("or")) {
					useAnd = false;
				}
			}

			List<Predicate> tagPredicates = new ArrayList<Predicate>();

			if (useAnd) {
				/* For AND searches, we need to AND together:
				 *   1. a separate subquery with EXISTS for each term in 'tags'
				 *   2. a separate subquery with NOT EXISTS for each term in 'notTags'
				 */
				for (String tag : tags) {
					Subquery<LTReferenceWorkflowTag> tagSubquery = query.subquery(LTReferenceWorkflowTag.class);
					Root<LTReferenceWorkflowTag> tagRoot = tagSubquery.from(LTReferenceWorkflowTag.class);
					tagSubquery.select(tagRoot);

					List<Predicate> inTags = new ArrayList<Predicate>();
					inTags.add(builder.equal(root.get("_refs_key"), tagRoot.get("_refs_key")));
					Path<String> column = tagRoot.get("tag").get("term");
					inTags.add(builder.equal(builder.lower(column), tag));

					tagSubquery.where(inTags.toArray(new Predicate[]{}));
					tagPredicates.add(builder.exists(tagSubquery));
				}

				for (String tag : notTags) {
					Subquery<LTReferenceWorkflowTag> tagSubquery = query.subquery(LTReferenceWorkflowTag.class);
					Root<LTReferenceWorkflowTag> tagRoot = tagSubquery.from(LTReferenceWorkflowTag.class);
					tagSubquery.select(tagRoot);

					List<Predicate> inTags = new ArrayList<Predicate>();
					inTags.add(builder.equal(root.get("_refs_key"), tagRoot.get("_refs_key")));
					Path<String> column = tagRoot.get("tag").get("term");
					inTags.add(builder.equal(builder.lower(column), tag));

					tagSubquery.where(inTags.toArray(new Predicate[]{}));
					tagPredicates.add(builder.not(builder.exists(tagSubquery)));
				}

				if (tagPredicates.size() > 0) {
					restrictions.add(builder.and(tagPredicates.toArray(new Predicate[0])));
				}
			} else {
				/* For OR searches, we need to OR together:
				 *   1. a single EXISTS subquery with all terms in 'tags' included in an IN
				 *   2. a NOT EXISTS subquery for each term in 'notTags' (see below when constructed)
				 */
				if (tags.size() > 0) {
					Subquery<LTReferenceWorkflowTag> tagSubquery = query.subquery(LTReferenceWorkflowTag.class);
					Root<LTReferenceWorkflowTag> tagRoot = tagSubquery.from(LTReferenceWorkflowTag.class);
					tagSubquery.select(tagRoot);

					List<Predicate> inTags = new ArrayList<Predicate>();
					inTags.add(builder.equal(root.get("_refs_key"), tagRoot.get("_refs_key")));
					Path<String> column = tagRoot.get("tag").get("term");
					Expression<String> lowerColumn = builder.lower(column);
					inTags.add(lowerColumn.in(tags));

					tagSubquery.where(inTags.toArray(new Predicate[]{}));
					tagPredicates.add(builder.exists(tagSubquery));
				}

				/* For NOT tags, we can't lump them into a single Exists clause, or we'd end up treating
				 * them like an AND.  Instead we need to have separate Exists queries, to be later OR-ed together.
				 */
				for (String notTag : notTags) {
					Subquery<LTReferenceWorkflowTag> tagSubquery = query.subquery(LTReferenceWorkflowTag.class);
					Root<LTReferenceWorkflowTag> tagRoot = tagSubquery.from(LTReferenceWorkflowTag.class);
					tagSubquery.select(tagRoot);

					List<Predicate> notInTags = new ArrayList<Predicate>();
					notInTags.add(builder.equal(root.get("_refs_key"), tagRoot.get("_refs_key")));
					Path<String> column = tagRoot.get("tag").get("term");
					Expression<String> lowerColumn = builder.lower(column);
					notInTags.add(builder.equal(lowerColumn, notTag));

					tagSubquery.where(notInTags.toArray(new Predicate[]{}));
					tagPredicates.add(builder.not(builder.exists(tagSubquery)));
				}

				if (tagPredicates.size() > 0) {
					restrictions.add(builder.or(tagPredicates.toArray(new Predicate[0])));
				}
			}
		}

		// search by term indicating whether reference has supplemental data or not

		if (params.containsKey("supplementalTerm") && (params.get("supplementalTerm") != null)) {
			Subquery<LTReferenceWorkflowData> rwdSubquery = query.subquery(LTReferenceWorkflowData.class);
			Root<LTReferenceWorkflowData> rwdRoot = rwdSubquery.from(LTReferenceWorkflowData.class);
			rwdSubquery.select(rwdRoot);

			List<Predicate> rwdPredicates = new ArrayList<Predicate>();

			rwdPredicates.add(builder.equal(root.get("_refs_key"), rwdRoot.get("_refs_key")));
			Join<LTReferenceWorkflowData,Term> supplementalTerm = rwdRoot.join("supplementalTerm");
			rwdPredicates.add(builder.equal(supplementalTerm.get("term"), params.get("supplementalTerm")));
			Join<LTReferenceWorkflowData,Term> extractedTextTerm = rwdRoot.join("extractedTextTerm");
			rwdPredicates.add(builder.equal(extractedTextTerm.get("term"), "body"));

			rwdSubquery.where(rwdPredicates.toArray(new Predicate[]{}));
			restrictions.add(builder.exists(rwdSubquery));
		}

		// enforce sorting: 1. J: number (descending), 2. journal (ascending), 3. author (ascending)

		List<Order> orderList = new ArrayList<Order>();
		Join<LTReference,ReferenceCitationCache> citationData = root.join("citationData");

		// using coalesce to push nulls (no J#) to bottom
		orderList.add(builder.desc(builder.coalesce(citationData.get("numericPart"), Integer.MIN_VALUE)));
		// then sort those at the bottom by ascending MGI ID (any without MGI ID go to bottom)
		orderList.add(builder.asc(builder.coalesce(citationData.get("mgiid"), "ZZZ")));
		query.orderBy(orderList);

		// pick up the row limit, if there is one specified.  If none specified, set default.

		int rowLimit = 1001;
		if (params.containsKey("row_limit")) {
			rowLimit = (Integer) params.get("row_limit");
		}

		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		log.debug(query.toString());
		log.debug(entityManager.createQuery(query).toString());

		// finally execute the query and return the list of results

		SearchResults<LTReference> results = new SearchResults<LTReference>();
		List<LTReference> refs = entityManager.createQuery(query).setMaxResults(rowLimit).getResultList();
		log.info("got " + refs.size() + " basic references");
		results.setItems(refs);

		// If we hit the row limit, then we should also compose and execute a query to get the total count
		// that could be returned by the query if there was a larger limit.

		if (refs.size() >= rowLimit) {
			CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
			countQuery.select(builder.count(countQuery.from(myClass)));
			countQuery.where(builder.and(restrictions.toArray(new Predicate[0])));
			long allMatchCount = entityManager.createQuery(countQuery).getSingleResult();
			log.info(" - of " + allMatchCount + " total matches");
			results.setAllMatchCount(allMatchCount);
		}

		return results;
	}

	/* get a list of the workflow status records for a reference
	 */
	public List<LTReferenceWorkflowStatus> getStatusHistory (String refsKey) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LTReferenceWorkflowStatus> query = builder.createQuery(LTReferenceWorkflowStatus.class);
		Root<LTReferenceWorkflowStatus> root = query.from(LTReferenceWorkflowStatus.class);

		query.where(builder.equal(root.get("_refs_key"), refsKey));
		query.orderBy(builder.desc(root.get("modification_date")));

		return entityManager.createQuery(query).getResultList();
	}

	/* return a single reference for the given reference key with all needed lazy-loaded fields already loaded
	 */
	@Transactional
	public LTReference getReference(String refsKey) throws APIException {
		LTReference ref =  entityManager.find(LTReference.class, Integer.valueOf(refsKey));
		if (ref == null) { return null; }
		Hibernate.initialize(ref.getWorkflowTags().size());
		Hibernate.initialize(ref.getReferenceTypeTerm());
		Hibernate.initialize(ref.getNotes());
		Hibernate.initialize(ref.getReferenceBook());
		Hibernate.initialize(ref.getCreatedByUser());
		Hibernate.initialize(ref.getModifiedByUser());
		Hibernate.initialize(ref.getAssociatedData());
		Hibernate.initialize(ref.getAccessionIDs());
		Hibernate.initialize(ref.getWorkflowData());
		Hibernate.initialize(ref.getWorkflowStatuses());
		return ref;
	}

	/* get the next available primary key for a workflow status record
	 */
	public synchronized int getNextWorkflowStatusKey() throws FatalAPIException {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_status_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* get the next available primary key for a workflow tag record
	 */
	public synchronized int getNextWorkflowTagKey() {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_tag_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* update the bib_citation_cache table for the given reference key
	 */
	public void updateCitationCache(String refsKey) {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select count(1) from BIB_reloadCache(" + refsKey + ")");
		query.getResultList();		
		return;
	}

	/* add a new J: number for the given reference key and user key
	 */
	public void assignNewJnumID(String refsKey, int userKey) throws Exception {
		// returns an integer rather than *, as the void return was causing a mapping exception
		log.info("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		Query query = entityManager.createNativeQuery("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		query.getResultList();
		return;
	}

	// get an integer version of the given (String) year
	private Integer getInteger(String year) throws FatalAPIException {
		try {
			return Integer.parseInt(year.trim());
		} catch (Exception e) {
			throw new FatalAPIException ("Non-integer year: " + year);
		}
	}

	// produce and return a JPA predicate that provides searching of 'year' field as either:
	//	1. a single integer
	//	2. a comma-delimited list of integers
	//	3. a range of two integers delimited by '..' (inclusive of the boundary years)
	//	4. an integer preceded by a relational operator:  >, <, >=, <=, =
	private Predicate yearPredicate(CriteriaBuilder builder, Expression<Integer> field, String year) throws FatalAPIException {
		if (year.startsWith("=")) {
			return builder.equal(field, year.substring(1));
		}

		if (year.startsWith(">=")) {
			return builder.greaterThanOrEqualTo(field, getInteger(year.substring(2)));
		}

		if (year.startsWith(">")) {
			return builder.greaterThan(field, getInteger(year.substring(1)));
		}

		if (year.startsWith("<=")) {
			return builder.lessThanOrEqualTo(field, getInteger(year.substring(2)));
		}

		if (year.startsWith("<")) {
			return builder.lessThan(field, getInteger(year.substring(1)));
		}

		// range of two years
		if (year.indexOf("..") >= 0) {
			String[] years = year.split("\\.\\.");
			if (years.length != 2) {
				throw new FatalAPIException("Expected format startYear..endYear");
			}
			return builder.between(field, getInteger(years[0]), getInteger(years[1]));
		}

		// comma-delimited list of integers

		if (year.indexOf(",") >= 0) {
			List<Integer> years = new ArrayList<Integer>();
			for (String yearStr : year.split(",")) {
				years.add(getInteger(yearStr));
			}
			return field.in(years);
		}

		// equals (implicit)
		return builder.equal(field, getInteger(year));
	}
	
	@Transactional	
	public SearchResults<LTReference> searchSQL(LTReferenceDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		SearchResults<LTReference> results = new SearchResults<LTReference>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct c._refs_key, c.jnumid, c.numericPart, c.short_citation";
		String from = "from bib_citation_cache c, bib_refs r";
		String where = "where c._refs_key = r._refs_key";
		String 	orderBy = "order by c.short_citation";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		
		Boolean from_note = false;
		Boolean from_book = false;
		Boolean from_accession = false;
		Boolean from_editAccession = false;
		
		//Boolean from_allele = false;
		//Boolean from_marker = false;
		//Boolean from_strain = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("r", searchDomain.getCreated_by(), searchDomain.getModified_by(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getJnumid() != null && !searchDomain.getJnumid().isEmpty()) {
			String jnumid = searchDomain.getJnumid().toUpperCase();
			if (!jnumid.contains("J:")) {
				jnumid = "J:" + jnumid;
			}
			where = where + "\nand c.jnumid = '" + jnumid + "'";
		}
		
		if (searchDomain.getReferenceTypeKey() != null && !searchDomain.getReferenceTypeKey().isEmpty()) {
			where = where + "\nand r._ReferenceType_key = " + searchDomain.getReferenceTypeKey();
		}
		if (searchDomain.getAuthors() != null && !searchDomain.getAuthors().isEmpty()) {
			where = where + "\nand r.authors ilike '" + searchDomain.getAuthors() + "'";
		}
		if (searchDomain.getTitle() != null && !searchDomain.getTitle().isEmpty()) {
			where = where + "\nand r.title ilike '" + searchDomain.getTitle() + "'";
		}
		if (searchDomain.getJournal() != null && !searchDomain.getJournal().isEmpty()) {
			where = where + "\nand r.journal ilike '" + searchDomain.getJournal() + "'";
		}
		if (searchDomain.getVol() != null && !searchDomain.getVol().isEmpty()) {
			where = where + "\nand r.vol ilike '" + searchDomain.getVol() + "'";
		}
		if (searchDomain.getIssue() != null && !searchDomain.getIssue().isEmpty()) {
			where = where + "\nand r.issue ilike '" + searchDomain.getIssue() + "'";
		}
		if (searchDomain.getPgs() != null && !searchDomain.getPgs().isEmpty()) {
			where = where + "\nand r.pgs ilike '" + searchDomain.getPgs() + "'";
		}
		if (searchDomain.getDate() != null && !searchDomain.getDate().isEmpty()) {
			where = where + "\nand r.date ilike '" + searchDomain.getDate() + "'";
		}
		if (searchDomain.getYear() != null && !searchDomain.getYear().isEmpty()) {
			where = where + "\nand r.year = " + searchDomain.getYear();
		}		
		if (searchDomain.getIsReviewArticle() != null && !searchDomain.getIsReviewArticle().isEmpty()) {
			where = where + "\nand r.isReviewArticle = " + searchDomain.getIsReviewArticle();
		}
		if (searchDomain.getIsDiscard() != null && !searchDomain.getIsDiscard().isEmpty()) {
			where = where + "\nand r.isDiscard = " + searchDomain.getIsDiscard();
		}
		if (searchDomain.getReferenceAbstract() != null && !searchDomain.getReferenceAbstract().isEmpty()) {
			where = where + "\nand r.abstract ilike '" + searchDomain.getReferenceAbstract() + "'";
		}
		
		// bib_books
		if (searchDomain.getBook_author() != null && !searchDomain.getBook_author().isEmpty()) {
			where = where + "\nand k.book_au ilike '" + searchDomain.getBook_author() + "'";
			from_book = true;
		}
		if (searchDomain.getBook_title() != null && !searchDomain.getBook_title().isEmpty()) {
			where = where + "\nand k.book_title ilike '" + searchDomain.getBook_title() + "'";
			from_book = true;
		}
		if (searchDomain.getPlace() != null && !searchDomain.getPlace().isEmpty()) {
			where = where + "\nand k.place ilike '" + searchDomain.getPlace() + "'";
			from_book = true;
		}
		if (searchDomain.getPublisher() != null && !searchDomain.getPublisher().isEmpty()) {
			where = where + "\nand k.publisher ilike '" + searchDomain.getPublisher() + "'";
			from_book = true;
		}
		if (searchDomain.getSeries_ed() != null && !searchDomain.getSeries_ed().isEmpty()) {
			where = where + "\nand k.series_ed ilike '" + searchDomain.getSeries_ed() + "'";
			from_book = true;
		}			
		
		// bib_notes
		if (searchDomain.getReferenceNote() != null && !searchDomain.getReferenceNote().isEmpty()) {
			where = where + "\nand n.note ilike '" + searchDomain.getReferenceNote() + "'";
			from_note = true;
		}
		
		// accession id
		if (searchDomain.getMgiid() != null && !searchDomain.getMgiid().isEmpty()) {
			String mgiid = searchDomain.getMgiid().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand a.accID ilike '" + mgiid + "'";
			from_accession = true;
		}
		
		// editable accession ids
//		if (searchDomain.getEditAccessionIds() != null) {
//			if (searchDomain.getEditAccessionIds().get(0).getAccID() != null 
//					&& !searchDomain.getEditAccessionIds().get(0).getAccID().isEmpty()) {
//				where = where + "\nand acc1.accID ilike '" +  searchDomain.getEditAccessionIds().get(0).getAccID() + "'";
//				from_editAccession = true;
//			}
//			if (searchDomain.getEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
//				where = where + "\nand acc1._logicaldb_key = " + searchDomain.getEditAccessionIds().get(0).getLogicaldbKey();
//				from_editAccession = true;
//			}
//		}
								
		if (from_book == true) {
			from = from + ", bib_books k";
			where = where + "\nand c._refs_key = k._refs_key";
		}
		if (from_note == true) {
			from = from + ", bib_notes n";
			where = where + "\nand c._refs_key = n._refs_key";
		}
		if (from_accession == true) {
			from = from + ", bib_acc_view a";
			where = where + "\nand c._refs_key = a._object_key" 
					+ "\nand a._mgitype_key = 1";
		}
		if (from_editAccession == true) {
			from = from + ", bib_acc_view acc1";
			where = where + "\nand acc1._logicaldb_key in (29, 65, 185)" +
					"\nand c._refs_key = acc1._object_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}

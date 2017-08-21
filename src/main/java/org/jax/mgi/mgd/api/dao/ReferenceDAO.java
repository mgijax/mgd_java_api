package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.entities.AccessionID;
import org.jax.mgi.mgd.api.entities.AccessionMax;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceAlleleAssociation;
import org.jax.mgi.mgd.api.entities.ReferenceMarkerAssociation;
import org.jax.mgi.mgd.api.entities.ReferenceNote;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowData;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowTag;
import org.jax.mgi.mgd.api.util.Constants;

@RequestScoped
public class ReferenceDAO extends PostgresSQLDAO<Reference> {

	// maps from search field name to workflow group abbreviation
	private static Map<String,String> groups = null;

	// maps from search field name to workflow group status
	private static Map<String,String> statuses = null;
	
	public ReferenceDAO() {
		myClass = Reference.class;
	}

	/* query handling specific for references.  Some fields are within the table backing Reference,
	 * while others are coming from related tables.
	 */
	public List<Reference> get(HashMap<String, Object> params) {
		// query parameters existing in main reference table
		List<String> internalParameters = new ArrayList<String>(Arrays.asList(
			new String[] { "issue", "pages", "date", "ref_abstract", "isReviewArticle", "title",
				"authors", "primary_author", "journal", "volume", "year", "_refs_key" }));
		
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

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Reference> query = builder.createQuery(myClass);
		Root<Reference> root = query.from(myClass);

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

		// special internal parameter -- is_discard.  QF specifies three values, which need to be translated
		// for the actual data in the bib_refs table.
		
		if (params.containsKey("is_discard")) {
			String desiredValue = ((String) params.get("is_discard")).toLowerCase();

			if (desiredValue.equals("no discard")) {
				restrictions.add(builder.equal(root.get("is_discard"), 0)); 
				
			} else if (desiredValue.equals("only discard")) {
				restrictions.add(builder.equal(root.get("is_discard"), 1)); 
				
			} else if (desiredValue.equals("search all")) {
				// disregard the is_discard flag when searching 
			}

		} else if (!params.containsKey("_refs_key")){
			// default setting is to only return non-discarded references -- only apply if we're not
			// doing a key-based lookup, though.
			restrictions.add(builder.equal(root.get("is_discard"), 0)); 
		}
		
		// second, handle list of status parameters
		
		List<Predicate> wfsRestrictions = new ArrayList<Predicate>();

		for (String key: statusParameters) {
			if (params.containsKey(key)) {
				String groupAbbrev = groups.get(key);
				String status = statuses.get(key);
				
				Subquery<ReferenceWorkflowStatus> wfsSubquery = query.subquery(ReferenceWorkflowStatus.class);
				Root<ReferenceWorkflowStatus> wfsRoot = wfsSubquery.from(ReferenceWorkflowStatus.class);
				wfsSubquery.select(wfsRoot);

				List<Predicate> wfsPredicates = new ArrayList<Predicate>();
				wfsPredicates.add(builder.equal(root.get("_refs_key"), wfsRoot.get("_refs_key")));
				wfsPredicates.add(builder.equal(wfsRoot.get("isCurrent"), 1));
				wfsPredicates.add(builder.equal(wfsRoot.get("groupTerm").get("abbreviation"), groupAbbrev));
				wfsPredicates.add(builder.equal(wfsRoot.get("statusTerm").get("term"), status));

				wfsSubquery.where(wfsPredicates.toArray(new Predicate[]{}));
				wfsRestrictions.add(builder.exists(wfsSubquery));
			}
		}
		if (wfsRestrictions.size() > 0) {
			restrictions.add(builder.or(wfsRestrictions.toArray(new Predicate[0])));
		}
		
		// third handle list of external parameters, including:
		//		"notes", "reference_type", "marker_id", "allele_id", "accids", "workflow_tag"
		
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
		
		if (params.containsKey("reference_type")) {
			restrictions.add(builder.equal(root.get("referenceTypeTerm").get("term"), params.get("reference_type")));
		}
		
		if (params.containsKey("accids")) {
			String idString = (String) params.get("accids");
			String[] accids = idString.toLowerCase().replaceAll(",", " ").replaceAll(" +", " ").split(" ");
			
			List<Predicate> idRestrictions = new ArrayList<Predicate>();

			Subquery<AccessionID> idSubquery = query.subquery(AccessionID.class);
			Root<AccessionID> idRoot = idSubquery.from(AccessionID.class);
			idSubquery.select(idRoot);
			
			List<Predicate> idPredicates = new ArrayList<Predicate>();
			
			idPredicates.add(builder.equal(root.get("_refs_key"), idRoot.get("_object_key")));
			Path<String> column = idRoot.get("accID");
			idPredicates.add(builder.lower(column).in((Object[]) accids));
			idPredicates.add(builder.equal(idRoot.get("_mgitype_key"), 1));

			idSubquery.where(idPredicates.toArray(new Predicate[]{}));
			idRestrictions.add(builder.exists(idSubquery));

			if (idRestrictions.size() > 0) {
				restrictions.add(builder.or(idRestrictions.toArray(new Predicate[0])));
			}
		}
		
		if (params.containsKey("marker_id")) {
			String idString = (String) params.get("marker_id");

			Subquery<ReferenceMarkerAssociation> idSubquery = query.subquery(ReferenceMarkerAssociation.class);
			Root<ReferenceMarkerAssociation> idRoot = idSubquery.from(ReferenceMarkerAssociation.class);
			idSubquery.select(idRoot);
			
			List<Predicate> idPredicates = new ArrayList<Predicate>();
				
			idPredicates.add(builder.equal(root.get("_refs_key"), idRoot.get("keys").get("_refs_key")));
			idPredicates.add(builder.equal(idRoot.get("markerID").get("accID"), idString));

			idSubquery.where(idPredicates.toArray(new Predicate[]{}));
			restrictions.add(builder.exists(idSubquery));
		}

		if (params.containsKey("allele_id")) {
			String idString = (String) params.get("allele_id");

			Subquery<ReferenceAlleleAssociation> idSubquery = query.subquery(ReferenceAlleleAssociation.class);
			Root<ReferenceAlleleAssociation> idRoot = idSubquery.from(ReferenceAlleleAssociation.class);
			idSubquery.select(idRoot);
			
			List<Predicate> idPredicates = new ArrayList<Predicate>();
				
			idPredicates.add(builder.equal(root.get("_refs_key"), idRoot.get("_refs_key")));
			idPredicates.add(builder.equal(idRoot.get("_mgitype_key"), 11));
			idPredicates.add(builder.equal(idRoot.get("alleleID").get("accID"), idString));
			idPredicates.add(builder.equal(idRoot.get("alleleID").get("_mgitype_key"), 11));

			idSubquery.where(idPredicates.toArray(new Predicate[]{}));
			restrictions.add(builder.exists(idSubquery));
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
					if (((String) params.get(notField)).trim().equalsIgnoreCase("not")) {
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
					Subquery<ReferenceWorkflowTag> tagSubquery = query.subquery(ReferenceWorkflowTag.class);
					Root<ReferenceWorkflowTag> tagRoot = tagSubquery.from(ReferenceWorkflowTag.class);
					tagSubquery.select(tagRoot);
			
					List<Predicate> inTags = new ArrayList<Predicate>();
					inTags.add(builder.equal(root.get("_refs_key"), tagRoot.get("_refs_key")));
					Path<String> column = tagRoot.get("tag").get("term");
					inTags.add(builder.equal(builder.lower(column), tag));

					tagSubquery.where(inTags.toArray(new Predicate[]{}));
					tagPredicates.add(builder.exists(tagSubquery));
				}

				for (String tag : notTags) {
					Subquery<ReferenceWorkflowTag> tagSubquery = query.subquery(ReferenceWorkflowTag.class);
					Root<ReferenceWorkflowTag> tagRoot = tagSubquery.from(ReferenceWorkflowTag.class);
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
				 *   2. a single NOT EXISTS subquery with all terms in 'notTags' included in an IN
				 */
				if (tags.size() > 0) {
					Subquery<ReferenceWorkflowTag> tagSubquery = query.subquery(ReferenceWorkflowTag.class);
					Root<ReferenceWorkflowTag> tagRoot = tagSubquery.from(ReferenceWorkflowTag.class);
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
					Subquery<ReferenceWorkflowTag> tagSubquery = query.subquery(ReferenceWorkflowTag.class);
					Root<ReferenceWorkflowTag> tagRoot = tagSubquery.from(ReferenceWorkflowTag.class);
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
		
		// search by extracted text (currently a case-insensitive 'contains' search on the string entered)
		
		if (params.containsKey("extracted_text") && (params.get("extracted_text") != null)) {
			String textString = ((String) params.get("extracted_text")).toLowerCase().trim();
			if (textString.trim() != "") {
				List<Predicate> wordPredicates = new ArrayList<Predicate>();
				
				for (String token : textString.split("\\s")) {
					Subquery<ReferenceWorkflowData> textSubquery = query.subquery(ReferenceWorkflowData.class);
					Root<ReferenceWorkflowData> textRoot = textSubquery.from(ReferenceWorkflowData.class);
					textSubquery.select(textRoot);
			
					List<Predicate> textPredicates = new ArrayList<Predicate>();
				
					textPredicates.add(builder.equal(root.get("_refs_key"), textRoot.get("_refs_key")));
					Path<String> column = textRoot.get("extracted_text");
					Expression<String> lowerColumn = builder.lower(column);
					textPredicates.add(builder.like(lowerColumn, "%" + token + "%"));

					textSubquery.where(textPredicates.toArray(new Predicate[]{}));
					wordPredicates.add(builder.exists(textSubquery));
				}
				restrictions.add(builder.and(wordPredicates.toArray(new Predicate[0])));
			}
		}
		
		// pick up the row limit, if there is one specified.  If none specified, set default.
		
		int rowLimit = 1001;
		if (params.containsKey("row_limit")) {
			rowLimit = (Integer) params.get("row_limit");
		}

		// finally execute the query and return the list of results
		
		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		log.debug(query.toString());
		log.debug(entityManager.createQuery(query).toString());

		return entityManager.createQuery(query).setMaxResults(rowLimit).getResultList();
	}
	
	/* get a list of the workflow status records for a reference
	 */
	public List<ReferenceWorkflowStatus> getStatusHistory (String refsKey) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ReferenceWorkflowStatus> query = builder.createQuery(ReferenceWorkflowStatus.class);
		Root<ReferenceWorkflowStatus> root = query.from(ReferenceWorkflowStatus.class);

		query.where(builder.equal(root.get("_refs_key"), refsKey));
		query.orderBy(builder.desc(root.get("modification_date"))); 

		return entityManager.createQuery(query).getResultList();
	}
	
	/* set the given workflow_tag for all references identified in the list of keys
	 */
	@Transactional
	public void updateInBulk(List<Long> refsKeys, String workflow_tag) throws Exception {
		if ((refsKeys == null) || (refsKeys.size() == 0) || (workflow_tag == null) || (workflow_tag.length() == 0)) {
			return; 
		}
		
		for (Long refsKey : refsKeys) {
			Reference reference = entityManager.find(Reference.class, refsKey);
			if (reference != null) {
				reference.addTag(workflow_tag, this);
			} else {
				throw new Exception("Unknown reference key: " + refsKey);
			}
		}
	}
	
	@Transactional
	public Reference update(ReferenceDomain referenceDomain) throws Exception {
		/*
		 * 1. retrieve the corresponding Reference object
		 * 2. update it with data from the ReferenceDomain object
		 * 3. persist the Reference object
		 * 4. return the Reference object
		 */
		Reference reference = entityManager.find(Reference.class, referenceDomain._refs_key);
		reference.applyDomainChanges(referenceDomain, this);
		entityManager.persist(reference);
		return reference;
	}
	
	/* get the next available primary key for a new reference
	 */
	public synchronized long getNextRefsKey() {
		return this.getNextKey("Reference", "_refs_key");
	}
	
	/* get the next available primary key for a workflow status record
	 */
	public synchronized long getNextWorkflowStatusKey() {
		return this.getNextKey("ReferenceWorkflowStatus", "_assoc_key");
	}
	
	/* get the next available primary key for a workflow tag record
	 */
	public synchronized long getNextWorkflowTagKey() {
		return this.getNextKey("ReferenceWorkflowTag", "_assoc_key");
	}
	
	/* get the next available J#
	 */
	public synchronized Long getNextJnum() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AccessionMax> query = builder.createQuery(AccessionMax.class);
		Root<AccessionMax> root = query.from(AccessionMax.class);
		query.where(builder.equal(root.get("prefixPart"), Constants.PREFIX_JNUM));
		AccessionMax accMax = entityManager.createQuery(query).getSingleResult();
		if (accMax == null) {
			return null;
		}
		
		accMax.maxNumericPart = accMax.maxNumericPart + 1;
		accMax.modification_date = new Date();
		this.persist(accMax);
		
		return accMax.maxNumericPart;
	}

	/* update the bib_citation_cache table for the given reference key
	 */
	public void updateCitationCache(long refsKey) {
		Query query = entityManager.createNativeQuery("select * from BIB_reloadCache(" + refsKey + ")");
		query.getResultList();
		return;
	}
}

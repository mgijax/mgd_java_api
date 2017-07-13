package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceNote;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.entities.Term;

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
			new String[] { "issue", "pages", "date", "abstract", "is_review", "title",
				"authors", "primary_author", "journal", "volume", "year" }));
		
		// non-status query parameters residing outside main reference table
		//List<String> externalParameters = new ArrayList<String>(Arrays.asList(
		//	new String[] { "notes", "reference_type", "marker_id", "allele_id", "accids", "mgi_discard" }));
		
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
			for (String status : new String[] { "Not_Routed", "Routed", "Indexed", "Chosen", "Fully_curated", "Rejected"}) {
				String fieldname = "status_" + group + "_" + status;
				statusParameters.add(fieldname);
				
				if (populateCaches) {
					groups.put(fieldname, group);
					statuses.put(fieldname, status.replace("_", " "));
				}
			}
		}

		/* if we handle status parameters before non-status parameters, we can keep a flag indicating whether
		 * some status search has been chosen.  If not, we need to look for the mgi_discard flag to find either:
		 * 1. (flag not set) references with at least one setting other than "Not Routed", or
		 * 2. (flag set) references with no settings other than "Not Routed"
		 */
		
		log.info("Reference Lookup: " + params);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Reference> query = builder.createQuery(myClass);
		Root<Reference> root = query.from(myClass);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		
		// first, handle the list of internal parameters (those that are in the table underlying Reference objects
		for (String key: internalParameters) {
			if (params.containsKey(key)) {
				Object desiredValue = params.get(key);

				// special handling for strings, depending on whether string contains a wildcard or any letters
				if (desiredValue instanceof String) {
					String valueString = (String) desiredValue;
					// has at least one wildcard, so do case-insensitive 'like' search
					if (valueString.indexOf("%") >= 0) {
						restrictions.add(builder.like(builder.lower(root.get(key)), valueString.toLowerCase()));

					} else if (valueString.matches(".*[A-Za-z].*")) {
						// no wildcards, but has at least one letter, so we know this needs to be a
						// case-insensitive equals search
						restrictions.add(builder.equal(builder.lower(root.get(key)), valueString.toLowerCase()));

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

		// second, handle list of status parameters
		
		boolean hasStatusParameter = false;
		List<Predicate> wfsRestrictions = new ArrayList<Predicate>();

		for (String key: statusParameters) {
			if (params.containsKey(key)) {
				hasStatusParameter = true;
				
				String groupAbbrev = groups.get(key);
				String status = statuses.get(key);
				
				Subquery<ReferenceWorkflowStatus> wfsSubquery = query.subquery(ReferenceWorkflowStatus.class);
				Root<ReferenceWorkflowStatus> wfsRoot = wfsSubquery.from(ReferenceWorkflowStatus.class);
				wfsSubquery.select(wfsRoot);

				List<Predicate> wfsPredicates = new ArrayList<Predicate>();
				wfsPredicates.add(builder.equal(root.get("_refs_key"), wfsRoot.get("_refs_key")));
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
		//		"notes", "reference_type", "marker_id", "allele_id", "accids", "mgi_discard"
		
		if (params.containsKey("notes")) {
				Subquery<ReferenceNote> noteSubquery = query.subquery(ReferenceNote.class);
				Root<ReferenceNote> noteRoot = noteSubquery.from(ReferenceNote.class);
				noteSubquery.select(noteRoot);

				List<Predicate> notePredicates = new ArrayList<Predicate>();
				notePredicates.add(builder.equal(root.get("_refs_key"), noteRoot.get("_refs_key")));
				notePredicates.add(builder.like(builder.lower(noteRoot.get("note")), ((String) params.get("notes")).toLowerCase()));

				noteSubquery.where(notePredicates.toArray(new Predicate[]{}));
				restrictions.add(builder.exists(noteSubquery));
		}
		
		// finally execute the query and return the list of results
		
		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		log.debug(query.toString());
		log.debug(entityManager.createQuery(query).toString());
		return entityManager.createQuery(query).getResultList();
	}
}

package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.enterprise.context.RequestScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.entities.AccessionID;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceAlleleAssociation;
import org.jax.mgi.mgd.api.entities.ReferenceMarkerAssociation;
import org.jax.mgi.mgd.api.entities.ReferenceNote;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;

@Singleton
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
			idPredicates.add(builder.lower(idRoot.get("accID")).in((Object[]) accids));
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

		// finally execute the query and return the list of results
		
		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		log.debug(query.toString());
		log.debug(entityManager.createQuery(query).toString());

		return entityManager.createQuery(query).getResultList();
	}
	
	/* get a list of the workflow status records for a reference
	 */
	public List<ReferenceWorkflowStatus> getStatusHistory (String refsKey) {
		log.info("Reference workflow status Lookup: refsKey = " + refsKey);

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ReferenceWorkflowStatus> query = builder.createQuery(ReferenceWorkflowStatus.class);
		Root<ReferenceWorkflowStatus> root = query.from(ReferenceWorkflowStatus.class);

		query.where(builder.equal(root.get("_refs_key"), refsKey));
		query.orderBy(builder.desc(root.get("modification_date"))); 

		return entityManager.createQuery(query).getResultList();
	}
	
	public Reference update(ReferenceDomain referenceDomain) {
		// Should the code to merge domain changes into the reference entity object be done here, where we
		// have access to the db through the entityManager?  Or should we pass the entityManager into the
		// reference object?  Seems like it needs to be done here...  Maybe provide individual merge methods
		// on a reference (basic fields, status, notes, etc.), including an updateStatus method that takes a
		// new workflow status key as one parameter.

		/*
		 * 1. retrieve the corresponding Reference object
		 * 2. update it with data from the ReferenceDomain object
		 * 3. persist the Reference object
		 * 4. return the Reference object
		 */
		Reference reference = entityManager.find(Reference.class, referenceDomain._refs_key);
		reference.applyDomainChanges(referenceDomain, this);
//		UserTransaction transaction = this.getTransaction();
//		try {
//			transaction.begin();
			entityManager.persist(reference);
//			transaction.commit();
//		} catch (Throwable e) {
//			try {
//				transaction.rollback();
//			} catch (IllegalStateException e1) {
//				e1.printStackTrace();
//			} catch (SecurityException e1) {
//				e1.printStackTrace();
//			} catch (SystemException e1) {
//				e1.printStackTrace();
//			}
//		}
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
}

package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.RelationshipDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class RelationshipService extends BaseService<RelationshipDomain> {

	protected static Logger log = Logger.getLogger(MGISynonymService.class);
	
	@Inject
	private RelationshipDAO relationshipDAO;

	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<RelationshipDomain> create(RelationshipDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<RelationshipDomain> update(RelationshipDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public RelationshipDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

    @Transactional
    public SearchResults<RelationshipDomain> getResults(Integer key) {
    	// TODO Auto-generated method stub
    	return null;
    }
    
	@Transactional
	public SearchResults<RelationshipDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RelationshipDomain> markerTSS(Integer key) {

		// list of results to be returned
		List<RelationshipDomain> results = new ArrayList<RelationshipDomain>();

		String cmd = "select * from mgi_relationship_markertss_view where _object_key = " + key;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// we could create a RelationshipMarkerTSSDomain
				// if we want more specific marker/object info
				// for now, we are just using the generic RelationshipDomain
				
				RelationshipDomain domain = new RelationshipDomain();
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setRelationshipKey(rs.getString("_relationship_key"));
				domain.setObjectKey1(rs.getString("_object_key_1"));
				domain.setObjectKey2(rs.getString("_object_key_2"));
				
				domain.setCategoryKey(rs.getString("_category_key"));
				domain.setCategoryTerm(rs.getString("categoryTerm"));
				domain.setRelationshipTermKey(rs.getString("_relationshipterm_key"));
				domain.setRelationshipTerm(rs.getString("relationshipTerm"));
				domain.setQualifierKey(rs.getString("_qualifier_key"));
				domain.setQualifierTerm(rs.getString("qualifierTerm"));
				domain.setEvidenceKey(rs.getString("_evidence_key"));
				domain.setEvidenceTerm(rs.getString("evidenceTerm"));
				
				domain.setRefKey(rs.getString("_refs_key"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setJnum(rs.getString("jnum"));
				domain.setShort_citation(rs.getString("short_citation"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdby"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedby"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
	
	@Transactional
	public void process(String parentKey, List<RelationshipDomain> domain, String mgiTypeKey, User user) {
		// process relationships (create, delete, update)
		
		if (domain == null || domain.isEmpty()) {
			log.info("processRelationships/nothing to process");
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// not fully implemented
				
				log.info("processRelationships create");

				//cmd = "select count(*) from MGI_insertSynonym ("
				//			+ user.get_user_key().intValue()
				//			+ "," + parentKey
				//			+ "," + mgiTypeKey
				//			//+ "," + domain.get(i).getSynonymTypeKey()
				//			//+ ",'" + domain.get(i).getSynonym() + "'"
				//			+ "," + domain.get(i).getRefKey()
				//			+ ")";
				log.info("cmd: " + cmd);
				//Query query = relationshipDAO.createNativeQuery(cmd);
				//query.getResultList();
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processRelationships delete");
				Relationship entity = relationshipDAO.get(Integer.valueOf(domain.get(i).getRelationshipKey()));
				relationshipDAO.remove(entity);
				log.info("processRelationships delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				
				// not fully implemented
				
				log.info("processRelationships update");

				Boolean modified = false;
				Relationship entity = relationshipDAO.get(Integer.valueOf(domain.get(i).getRelationshipKey()));
		
				//if (!entity.getSynonym().equals(domain.get(i).getRelationshipKey())) {
				//	entity.setSynonym(domain.get(i).getRelationshipKey());
				//	modified = true;
				//}
				
				if (modified == true) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					relationshipDAO.update(entity);
					log.info("processRelationships/changes processed: " + domain.get(i).getRelationshipKey());
				}
				else {
					log.info("processRelationships/no changes processed: " + domain.get(i).getRelationshipKey());
				}
			}
			else {
				log.info("processRelationships/no changes processed: " + domain.get(i).getRelationshipKey());
			}
		}
		
		log.info("processRelationships/processing successful");
		return;
	}
	
}

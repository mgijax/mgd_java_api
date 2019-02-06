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
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class RelationshipService extends BaseService<RelationshipDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private RelationshipDAO relationshipDAO;

	private RelationshipTranslator translator = new RelationshipTranslator();
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

	@Transactional	
	public List<RelationshipDomain> getMarkerTSS(Integer key) {
		// return all tss-marker relationships by specified marker key
		
		List<RelationshipDomain> results = new ArrayList<RelationshipDomain>();

		String cmd = "select * from mgi_relationship_markertss_view "
				+ "\nwhere _object_key_1 = " + key
				+ "\nor _object_key_2 = " + key;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// we could create a RelationshipMarkerTSSDomain
				// if we want more specific marker/object info
				// for now, we are just using the generic RelationshipDomain
				
				RelationshipDomain domain = new RelationshipDomain();
				domain = translator.translate(relationshipDAO.get(rs.getInt("_relationship_key")),1);
				relationshipDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
				//			+ "," + domain.get(i).getRefsKey()
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

package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISynonymService extends BaseService<MGISynonymDomain> {

	protected static Logger log = Logger.getLogger(MGISynonymService.class);
	
	@Inject
	private MGISynonymDAO synonymDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGISynonymDomain> create(MGISynonymDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGISynonymDomain> update(MGISynonymDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGISynonymDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

    @Transactional
    public SearchResults<MGISynonymDomain> getResults(Integer key) {
    	// TODO Auto-generated method stub
    	return null;
    }
    
	@Transactional
	public SearchResults<MGISynonymDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MGISynonymDomain> marker(Integer key) {

		// list of results to be returned
		List<MGISynonymDomain> results = new ArrayList<MGISynonymDomain>();

		String cmd = "\nselect * from mgi_synonym_musmarker_view where _object_key = " + key;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISynonymDomain domain = new MGISynonymDomain();
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setSynonymKey(rs.getString("_synonym_key"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
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
	public void process(String parentKey, List<MGISynonymDomain> domain, String mgiTypeKey, User user) {
		// process synonym associations (create, delete, update)
		
		if (domain == null || domain.isEmpty()) {
			log.info("processSynonym/nothing to process");
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				log.info("processSynonym create");

				cmd = "select count(*) from MGI_insertSynonym ("
							+ user.get_user_key().intValue()
							+ "," + parentKey
							+ "," + mgiTypeKey
							+ "," + domain.get(i).getSynonymTypeKey()
							+ ",'" + domain.get(i).getSynonym() + "'"
							+ "," + domain.get(i).getRefKey()
							+ ")";
				log.info("cmd: " + cmd);
				Query query = synonymDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processSynonym delete");
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
				synonymDAO.remove(entity);
				log.info("processSynonym delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processSynonym update");

				Boolean modified = false;
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
		
				if (!entity.getSynonym().equals(domain.get(i).getSynonym())) {
					entity.setSynonym(domain.get(i).getSynonym());
					modified = true;
				}
				
				// reference can be null
				// may be null coming from entity
				if (entity.getReference() == null) {
					if (!domain.get(i).getRefKey().isEmpty()) {
						entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
						modified = true;
					}
				}
				// may be empty coming from domain
				else if (domain.get(i).getRefKey().isEmpty()) {
					entity.setReference(null);
					modified = true;
				}
				// if not entity/null and not domain/empty, then check if equivalent
				else if (!entity.getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getRefKey()))) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
					modified = true;
				}
				
				if (modified == true) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					synonymDAO.update(entity);
					log.info("processSynonym/changes processed: " + domain.get(i).getSynonymKey());
				}
				else {
					log.info("processSynonym/no changes processed: " + domain.get(i).getSynonymKey());
				}
			}
			else {
				log.info("processSynonym/no changes processed: " + domain.get(i).getSynonymKey());
			}
		}
		
		log.info("processSynonym/processing successful");
		return;
	}
	
}

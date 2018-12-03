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
import org.jax.mgi.mgd.api.model.mgi.dao.MGIReferenceAssocDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIReferenceAssocService extends BaseService<MGIReferenceAssocDomain> {

	protected static Logger log = Logger.getLogger(MGIReferenceAssocService.class);
	
	@Inject
	private MGIReferenceAssocDAO referenceAssocDAO;
	
	private MGIReferenceAssocTranslator translator = new MGIReferenceAssocTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGIReferenceAssocDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> getResults(Integer key) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results.setItem(translator.translate(referenceAssocDAO.get(key)));
		return results;
	}
    
	@Transactional
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MGIReferenceAssocDomain> marker(Integer key) {

		// list of results to be returned
		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();

		String cmd = "select * from mgi_reference_marker_view where _object_key = " + key;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
				domain.setAssocKey(rs.getString("_assoc_key"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setRefAssocTypeKey(rs.getString("_refassoctype_key"));
				domain.setRefAssocType(rs.getString("assoctype"));
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
	public static void processReferenceAssoc(String parentKey, List<MGIReferenceAssocDomain> domain, MGIReferenceAssocDAO refAssocDAO, ReferenceDAO referenceDAO, String mgiTypeKey, User user) {
		// process reference associations (create, delete, update)
		
		if (domain == null || domain.isEmpty()) {
			log.info("processReferenceAssoc/nothing to process");
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getAssocKey() == null 
					|| domain.get(i).getAssocKey().isEmpty()) {
				
				log.info("processReferenceAssoc create");

				cmd = "select count(*) from MGI_insertReferenceAssoc ("
							+ user.get_user_key().intValue()
							+ "," + mgiTypeKey
							+ "," + parentKey
							+ "," + domain.get(i).getRefKey()
							+ ",'" + domain.get(i).getRefAssocType() + "'"
							+ ")";
				log.info("cmd: " + cmd);
				Query query = refAssocDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getRefKey() == null || domain.get(i).getRefKey().isEmpty()) {
				log.info("processReferenceAssoc delete");
				MGIReferenceAssoc entity = refAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				refAssocDAO.remove(entity);
				log.info("processReferenceAssoc delete successful");
			}
			else {
				log.info("processReferenceAssoc update");

				Boolean modified = false;
				MGIReferenceAssoc entity = refAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
		
				if (!entity.getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getRefKey()))) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
					modified = true;
				}
				
				if (modified == true) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					refAssocDAO.update(entity);
					log.info("processReferenceAssoc/changes processed: " + domain.get(i).getAssocKey());
				}
				else {
					log.info("processReferenceAssoc/no changes processed: " + domain.get(i).getAssocKey());
				}
			}
		}
		
		log.info("processReferenceAssoc/processing successful");
		return;
	}
	
}

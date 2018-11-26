package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIReferenceAssocDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIReferenceAssocService extends BaseService<MGIReferenceAssocDomain> {

	protected static Logger log = Logger.getLogger(MGIReferenceAssocService.class);
	
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
    	// TODO Auto-generated method stub
    	return null;
    }
    
	@Transactional
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
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
							+ "," + domain.get(i).getRefAssocTypeKey()
							+ ")";
				log.info("cmd: " + cmd);
				Query query = refAssocDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getRefKey().isEmpty()) {
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

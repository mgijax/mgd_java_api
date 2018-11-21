package org.jax.mgi.mgd.api.model.mgi.service;

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
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISynonymService extends BaseService<MGISynonymDomain> {

	protected static Logger log = Logger.getLogger(MGISynonymService.class);

	@Inject
	private static ReferenceDAO referenceDAO;
		
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

	@Transactional
	public void processSynonym(String parentKey, List<MGISynonymDomain> domain, MGISynonymDAO synonymDAO, ReferenceDAO referenceDAO, User user) {
		// create marker synonym associations
		
		if (domain == null || domain.isEmpty()) {
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getSynonymKey() == null 
					|| domain.get(i).getSynonymKey().isEmpty()) {
				//cmd = "select count(*) from MRK_insertHistory ("
				//			+ user.get_user_key().intValue()
				//			+ "," + parentKey
				//			+ "," + domain.get(i).getMarkerHistorySymbolKey()
				//			+ "," + domain.get(i).getRefKey()
				//			+ "," + domain.get(i).getMarkerEventKey()
				//			+ "," + domain.get(i).getMarkerEventReasonKey()
				//			+ ",'" + domain.get(i).getMarkerHistoryName() + "'"
				//			+ ")";
				log.info("cmd: " + cmd);
				Query query = synonymDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getSynonym().isEmpty()) {
				// process delete
				
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
				synonymDAO.remove(entity);
				log.info("processSynonym delete successful");
			}
			else {
				// process update

				Boolean modified = false;
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));

				//if (!String.valueOf(entity.get_marker_key()).equals(domain.get(i).getMarkerKey())) {
				//	entity.set_marker_key(Integer.valueOf(domain.get(i).getMarkerKey()));
				//	modified = true;
				//}
				
				//if (!entity.getMarkerEvent().equals(eventDAO.get(Integer.valueOf(domain.get(i).getMarkerEventKey())))) {
				//	entity.setMarkerEvent(eventDAO.get((Integer.valueOf(domain.get(i).getMarkerEventKey()))));
				//	modified = true;
				//}
				
				//if (!entity.getMarkerEventReason().equals(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())))) {
				//	entity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())));
				//	modified = true;
				//}
				
				// reference can be null
				if (!(entity.getReference() == null && domain.get(i).getRefKey() == null)) {
					if (!entity.getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getRefKey()))) {
						entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
						modified = true;
					}
				}
				else if (domain.get(i).getRefKey() == null) {
					entity.setReference(null);
					modified = true;
				}
				else {
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
		}
		
		log.info("processSynonym/ran successfully");
		return;
	}
	
}

package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetMemberDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetMemberTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class MGISetMemberService extends BaseService<MGISetMemberDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGISetMemberDAO setMemberDAO;

	private MGISetMemberTranslator translator = new MGISetMemberTranslator();				

	@Transactional
	public SearchResults<MGISetMemberDomain> create(MGISetMemberDomain domain, User user) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetMemberDomain> update(MGISetMemberDomain domain, User user) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetMemberDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		MGISetMember entity = setMemberDAO.get(key);
		results.setItem(translator.translate(setMemberDAO.get(key)));
		setMemberDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public MGISetMemberDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGISetMemberDomain domain = new MGISetMemberDomain();
		if (setMemberDAO.get(key) != null) {
			domain = translator.translate(setMemberDAO.get(key));
		}
		setMemberDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MGISetMemberDomain> getResults(Integer key) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setItem(translator.translate(setMemberDAO.get(key)));
		setMemberDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, List<MGISetMemberDomain> domain, User user) {
		// process set member (create, delete, update)
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processSetMember/nothing to process");
			return modified;
		}
				
		String cmd = "";
		Query query;
        String labelValue; // for escaping of the set label
        int stageValue = 0;

		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// empty label is allowed; don't skip
				//if (domain.get(i).getLabel() == null || domain.get(i).getLabel().isEmpty()) {
				//	continue;
				//}
				
				log.info("processSetMember create");

				// only 1 emapa member is ever used
				if (domain.get(i).getEmapaStage() != null) {
					stageValue = Integer.valueOf(domain.get(i).getEmapaStage().getStage());
				}
				
				labelValue = domain.get(i).getLabel().replace("'",  "''");
				
				cmd = "select count(*) from MGI_addSetMember ("
						+ domain.get(i).getSetKey()
						+ "," + domain.get(i).getObjectKey()
						+ "," + user.get_user_key() 
						+ ", '" + labelValue + "'"
						+ "," + stageValue
						+")";
				log.info(cmd);
				query = setMemberDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processSetMember delete");
				MGISetMember entity = setMemberDAO.get(Integer.valueOf(domain.get(i).getSetMemberKey()));
				setMemberDAO.remove(entity);
				modified = true;
				log.info("processSetMember delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processSetMember update/sequenceNum");
				cmd = "select count(*) from MGI_updateSetMember ("
						+ domain.get(i).getSetMemberKey()
						+ "," + domain.get(i).getSequenceNum()
						+")";
				log.info(cmd);
				query = setMemberDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
				log.info("processSetMember/changes processed: " + domain.get(i).getSetMemberKey());
			}
			else {
				log.info("processSetMember/no changes processed: " + domain.get(i).getSetMemberKey());
			}
		}
		
		log.info("processSetMember/processing successful");
		return modified;
	}
    
}

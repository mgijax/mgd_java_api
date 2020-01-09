package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetMemberDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetMemberTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISetMemberService extends BaseService<MGISetMemberDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGISetMemberDAO setMemberDAO;


	private MGISetMemberTranslator translator = new MGISetMemberTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGISetMemberDomain> create(MGISetMemberDomain object, User user) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetMemberDomain> update(MGISetMemberDomain object, User user) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetMemberDomain> delete(Integer key, User user) {
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
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
	public Boolean process(String parentKey, List<MGISetMemberDomain> domain, String mgiTypeKey, User user) {
		// process set member associations (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processSetMember/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
//			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
//	
//				// if set member is null/empty, then skip
//				// pwi has sent a "c" that is empty/not being used
//				if (domain.get(i).getSetMember() == null || domain.get(i).getSetMember().isEmpty()) {
//					continue;
//				}
//				
//				log.info("processSynonym create : " + mgiTypeKey);
//				
//				// if mgiTypeKey = marker, then set default synonym type "exact" (1004)
//				String synonymTypeKey = domain.get(i).getSynonymTypeKey();
//				if (mgiTypeKey.equals("2")) {
//					if (synonymTypeKey == null || synonymTypeKey.isEmpty()) {
//						synonymTypeKey = "1004";	
//					}
//				}
//				
//				cmd = "select count(*) from MGI_insertSynonym ("
//							+ user.get_user_key().intValue()
//							+ "," + parentKey
//							+ "," + mgiTypeKey
//							+ "," + synonymTypeKey
//							+ ",'" + domain.get(i).getSynonym() + "'"
//							+ "," + domain.get(i).getRefsKey()
//							+ ",0)";
//				log.info("cmd: " + cmd);
//				Query query = synonymDAO.createNativeQuery(cmd);
//				query.getResultList();
//				modified = true;
//			}
//			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
//				log.info("processSynonym delete");
//				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
//				synonymDAO.remove(entity);
//				modified = true;
//				log.info("processSynonym delete successful");
//			}
//			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
//				log.info("processSynonym update");
//
//				Boolean isUpdated = false;
//				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
//		
//				if (!entity.getSynonym().equals(domain.get(i).getSynonym())) {
//					entity.setSynonym(domain.get(i).getSynonym());
//					isUpdated = true;
//				}
//				
//				// reference can be null
//				// may be null coming from entity
//				if (entity.getReference() == null) {
//					if (!domain.get(i).getRefsKey().isEmpty()) {
//						entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
//						isUpdated = true;
//					}
//				}
//				// may be empty coming from domain
//				else if (domain.get(i).getRefsKey().isEmpty()) {
//					entity.setReference(null);
//					isUpdated = true;
//				}
//				// if not entity/null and not domain/empty, then check if equivalent
//				else if (entity.getReference().get_refs_key() != Integer.parseInt(domain.get(i).getRefsKey())) {
//					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
//					isUpdated = true;
//				}
//				
//				if (isUpdated) {
//					entity.setModification_date(new Date());
//					entity.setModifiedBy(user);
//					synonymDAO.update(entity);
//					modified = true;
//					log.info("processSynonym/changes processed: " + domain.get(i).getSynonymKey());
//				}
//				else {
//					log.info("processSynonym/no changes processed: " + domain.get(i).getSynonymKey());
//				}
//			}
//			else {
//				log.info("processSynonym/no changes processed: " + domain.get(i).getSynonymKey());
//			}
		}
		
		log.info("processSynonym/processing successful");
		return modified;
	}
	
}

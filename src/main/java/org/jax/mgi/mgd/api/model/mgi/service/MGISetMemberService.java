package org.jax.mgi.mgd.api.model.mgi.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetMemberDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetMemberTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISetMemberService extends BaseService<MGISetMemberDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGISetMemberDAO setMemberDAO;


	private MGISetMemberTranslator translator = new MGISetMemberTranslator();				

	@Transactional
	public SearchResults<MGISetMemberDomain> create(MGISetMemberDomain domain, User user) {
		// create new entity object from in-coming domain
		
		SearchResults<MGISetMemberDomain> results = new SearchResults<MGISetMemberDomain>();
		MGISetMember entity = new MGISetMember();
		String cmd;
		Query query;
		
		log.info("processSetMember/create");
		
		cmd = "select count(*) from MGI_addSetMember ("
				+ domain.getSetKey()
				+ "," + domain.getObjectKey()
				+ "," + user.get_user_key() 
				+")";
		query = setMemberDAO.createNativeQuery(cmd);
		query.getResultList();
		
		// return entity translated to domain
		log.info("processSetMember/create/returning results");
		results.setItem(translator.translate(entity));
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

}

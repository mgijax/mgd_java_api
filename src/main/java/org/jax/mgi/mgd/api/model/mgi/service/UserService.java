package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.UserDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.UserSearchForm;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class UserService extends BaseService<UserDomain> {

	@Inject
	private UserDAO userDAO;
	
	private Logger log = Logger.getLogger(getClass());

	@Transactional
	public SearchResults<UserDomain> create(UserDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<UserDomain> update(UserDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public UserDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

        @Transactional
        public SearchResults<UserDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
		return null;
        }
    
	@Transactional
	public SearchResults<UserDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<UserDomain> search(UserSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* get the User object corresponding to the given username (Linux login) */
	public User getUserByUsername(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", username);
		if(userDAO.search(map).total_count > 0) {
			log.info("User found: " + username);
			return userDAO.search(map).items.get(0);
		} else {
			log.info("User NOT found: " + username + " sending back mgd_dbo user");
			map.put("login", "mgd_dbo");
			return userDAO.search(map).items.get(0);
		}
	}

}

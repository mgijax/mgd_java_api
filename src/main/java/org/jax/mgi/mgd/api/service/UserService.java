package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.dao.UserDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class UserService extends ServiceInterface<UserDomain> {

	@Inject
	private UserDAO userDAO;

	@Override
	public UserDomain create(UserDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDomain update(UserDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<UserDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<UserDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* get the User object corresponding to the given username (Linux login)
	 */
	public User getUser(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", username);
		if(userDAO.search(map).total_count > 0) {
			return userDAO.search(map).items.get(0);
		} else {
			map.put("login", "mgd_dbo");
			return userDAO.search(map).items.get(0);
		}
	}

}

package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.dao.UserDAO;
import org.jax.mgi.mgd.api.entities.User;

@RequestScoped
public class UserService extends ServiceInterface<User> {

	@Inject
	private UserDAO userDAO;
	
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

	@Override
	public PostgresSQLDAO<User> getDAO() {
		return userDAO;
	}
}

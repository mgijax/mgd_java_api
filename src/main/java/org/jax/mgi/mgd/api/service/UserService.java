package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.dao.UserDAO;
import org.jax.mgi.mgd.api.entities.User;

@RequestScoped
public class UserService extends ServiceInterface<User> {

	@Inject
	private UserDAO userDAO;
	
	public User getUser(String username) {
		return userDAO.get(username);
	}

	@Override
	public PostgresSQLDAO<User> getDAO() {
		return userDAO;
	}
}

package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.UserDAO;
import org.jax.mgi.mgd.api.entities.User;

@RequestScoped
public class UserService {

	@Inject
	private UserDAO userDAO;
	
	public User getUser(String username) {
		return userDAO.get(username);
	}
}

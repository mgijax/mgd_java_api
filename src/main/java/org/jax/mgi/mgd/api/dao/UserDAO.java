package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.User;

@RequestScoped
public class UserDAO extends PostgresSQLDAO<User> {

	public UserDAO() {
		super(User.class);
	}

}

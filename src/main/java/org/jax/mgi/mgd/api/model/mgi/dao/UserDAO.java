package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

public class UserDAO extends PostgresSQLDAO<User> {
	public UserDAO() {
		super(User.class);
	}
}

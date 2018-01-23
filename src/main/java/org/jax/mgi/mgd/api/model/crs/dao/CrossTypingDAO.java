package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossTyping;

@RequestScoped
public class CrossTypingDAO extends PostgresSQLDAO<CrossTyping> {

	public CrossTypingDAO() {
		super(CrossTyping.class);
	}

}

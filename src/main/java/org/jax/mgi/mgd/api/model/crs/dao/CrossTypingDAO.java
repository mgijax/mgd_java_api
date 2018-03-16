package org.jax.mgi.mgd.api.model.crs.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossTyping;

public class CrossTypingDAO extends PostgresSQLDAO<CrossTyping> {
	public CrossTypingDAO() {
		super(CrossTyping.class);
	}
}

package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMemberEmapa;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGISetMemberEmapaDAO extends PostgresSQLDAO<MGISetMemberEmapa> {
	public MGISetMemberEmapaDAO() {
		super(MGISetMemberEmapa.class);
	}
}
package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGISetMemberDAO extends PostgresSQLDAO<MGISetMember> {
	public MGISetMemberDAO() {
		super(MGISetMember.class);
	}
}
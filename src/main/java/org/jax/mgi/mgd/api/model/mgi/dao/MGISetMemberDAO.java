package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;

@RequestScoped
public class MGISetMemberDAO extends PostgresSQLDAO<MGISetMember> {

	public MGISetMemberDAO() {
		super(MGISetMember.class);
	}

}

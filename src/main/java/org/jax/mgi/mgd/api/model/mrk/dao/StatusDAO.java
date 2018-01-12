package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Status;

@RequestScoped
public class StatusDAO extends PostgresSQLDAO<Status> {

	protected StatusDAO() {
		super(Status.class);
	}


}

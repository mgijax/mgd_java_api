package org.jax.mgi.mgd.api.model.seq.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.GeneTrap;

@RequestScoped
public class GeneTrapDAO extends PostgresSQLDAO<GeneTrap> {

	protected GeneTrapDAO() {
		super(GeneTrap.class);
	}


}

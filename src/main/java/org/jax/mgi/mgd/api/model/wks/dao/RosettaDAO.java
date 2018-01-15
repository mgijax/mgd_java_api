package org.jax.mgi.mgd.api.model.wks.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.wks.entities.Rosetta;

@RequestScoped
public class RosettaDAO extends PostgresSQLDAO<Rosetta> {

	public RosettaDAO() {
		super(Rosetta.class);
	}

}

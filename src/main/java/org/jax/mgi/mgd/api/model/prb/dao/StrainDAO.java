package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Strain;

@RequestScoped
public class StrainDAO extends PostgresSQLDAO<Strain> {

	protected StrainDAO() {
		super(Strain.class);
	}


}

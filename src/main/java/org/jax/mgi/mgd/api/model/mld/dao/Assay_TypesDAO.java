package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Assay_Types;

@RequestScoped
public class Assay_TypesDAO extends PostgresSQLDAO<Assay_Types> {

	public Assay_TypesDAO() {
		super(Assay_Types.class);
	}

}

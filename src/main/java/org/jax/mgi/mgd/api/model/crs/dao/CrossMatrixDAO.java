package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossMatrix;

@RequestScoped
public class CrossMatrixDAO extends PostgresSQLDAO<CrossMatrix> {

	public CrossMatrixDAO() {
		super(CrossMatrix.class);
	}

}

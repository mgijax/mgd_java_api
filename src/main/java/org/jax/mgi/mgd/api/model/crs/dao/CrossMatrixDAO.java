package org.jax.mgi.mgd.api.model.crs.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossMatrix;

public class CrossMatrixDAO extends PostgresSQLDAO<CrossMatrix> {
	public CrossMatrixDAO() {
		super(CrossMatrix.class);
	}
}

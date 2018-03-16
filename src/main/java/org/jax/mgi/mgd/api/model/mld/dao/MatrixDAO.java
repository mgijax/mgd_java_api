package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Matrix;

public class MatrixDAO extends PostgresSQLDAO<Matrix> {
	protected MatrixDAO() {
		super(Matrix.class);
	}
}

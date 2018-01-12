package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Matrix;

@RequestScoped
public class MatrixDAO extends PostgresSQLDAO<Matrix> {

	protected MatrixDAO() {
		super(Matrix.class);
	}


}

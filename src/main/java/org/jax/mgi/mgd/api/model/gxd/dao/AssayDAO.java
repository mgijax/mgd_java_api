package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class AssayDAO extends PostgresSQLDAO<Assay> {
	protected AssayDAO() {
		super(Assay.class);
	}
}

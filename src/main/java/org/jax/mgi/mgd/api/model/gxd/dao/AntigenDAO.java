package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Antigen;

public class AntigenDAO extends PostgresSQLDAO<Antigen> {
	protected AntigenDAO() {
		super(Antigen.class);
	}
}

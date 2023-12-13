package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultCellType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InSituResultCellTypeDAO extends PostgresSQLDAO<InSituResultCellType> {
	protected InSituResultCellTypeDAO() {
		super(InSituResultCellType.class);
	}
}

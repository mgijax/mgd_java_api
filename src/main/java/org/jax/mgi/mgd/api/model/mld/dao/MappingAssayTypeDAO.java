package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MappingAssayType;

public class MappingAssayTypeDAO extends PostgresSQLDAO<MappingAssayType> {
	public MappingAssayTypeDAO() {
		super(MappingAssayType.class);
	}
}

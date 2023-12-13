package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MappingAssayType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MappingAssayTypeDAO extends PostgresSQLDAO<MappingAssayType> {
	public MappingAssayTypeDAO() {
		super(MappingAssayType.class);
	}
}

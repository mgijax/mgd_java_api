package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InSituResultStructureDAO extends PostgresSQLDAO<InSituResultStructure> {
	protected InSituResultStructureDAO() {
		super(InSituResultStructure.class);
	}
}

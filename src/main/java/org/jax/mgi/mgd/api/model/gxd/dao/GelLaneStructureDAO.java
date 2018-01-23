package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLaneStructure;

@RequestScoped
public class GelLaneStructureDAO extends PostgresSQLDAO<GelLaneStructure> {

	protected GelLaneStructureDAO() {
		super(GelLaneStructure.class);
	}


}

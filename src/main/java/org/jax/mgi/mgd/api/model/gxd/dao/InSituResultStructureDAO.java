package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure;

@RequestScoped
public class InSituResultStructureDAO extends PostgresSQLDAO<InSituResultStructure> {

	protected InSituResultStructureDAO() {
		super(InSituResultStructure.class);
	}


}

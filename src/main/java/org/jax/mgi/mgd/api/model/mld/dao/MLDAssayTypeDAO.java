package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.AssayTypes;

@RequestScoped
public class AssayTypesDAO extends PostgresSQLDAO<AssayTypes> {

	public AssayTypesDAO() {
		super(AssayTypes.class);
	}

}

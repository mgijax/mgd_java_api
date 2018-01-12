package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.BiotypeMapping;

@RequestScoped
public class BiotypeMappingDAO extends PostgresSQLDAO<BiotypeMapping> {

	protected BiotypeMappingDAO() {
		super(BiotypeMapping.class);
	}


}

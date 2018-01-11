package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Index;

@RequestScoped
public class IndexDAO extends PostgresSQLDAO<Index> {

	protected IndexDAO() {
		super(Index.class);
	}


}

package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DAG;

@RequestScoped
public class DAGDAO extends PostgresSQLDAO<DAG> {

	protected DAGDAO() {
		super(DAG.class);
	}


}

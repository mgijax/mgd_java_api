package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Cluster;

@RequestScoped
public class ClusterDAO extends PostgresSQLDAO<Cluster> {

	protected ClusterDAO() {
		super(Cluster.class);
	}


}

package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Cluster;

public class ClusterDAO extends PostgresSQLDAO<Cluster> {
	protected ClusterDAO() {
		super(Cluster.class);
	}
}

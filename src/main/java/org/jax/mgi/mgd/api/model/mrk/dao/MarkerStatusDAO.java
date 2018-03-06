package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerStatus;

public class MarkerStatusDAO extends PostgresSQLDAO<MarkerStatus> {
	protected MarkerStatusDAO() {
		super(MarkerStatus.class);
	}
}

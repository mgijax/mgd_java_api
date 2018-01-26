package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerStatus;

@RequestScoped
public class MarkerStatusDAO extends PostgresSQLDAO<MarkerStatus> {

	protected MarkerStatusDAO() {
		super(MarkerStatus.class);
	}


}

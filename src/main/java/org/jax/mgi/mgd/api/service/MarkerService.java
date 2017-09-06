package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.MarkerDAO;
import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.entities.Marker;

@RequestScoped
public class MarkerService extends ServiceInterface<Marker> {

	@Inject
	private MarkerDAO markerDAO;
	
	@Override
	public PostgresSQLDAO<Marker> getDAO() {
		return markerDAO;
	}



}

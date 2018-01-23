package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

@RequestScoped
public class MarkerService extends ServiceInterface<Marker> {

	@Inject
	private MarkerDAO markerDAO;
	
	@Override
	public PostgresSQLDAO<Marker> getDAO() {
		return markerDAO;
	}



}

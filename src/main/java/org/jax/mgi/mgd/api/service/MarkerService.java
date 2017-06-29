package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.MarkerDAO;
import org.jax.mgi.mgd.api.entities.Marker;

@RequestScoped
public class MarkerService {

	@Inject
	private MarkerDAO markerDAO;
	
	public Marker createMarker(Marker marker) {
		return markerDAO.add(marker);
	}

	public Marker updateMarker(Marker marker) {
		return markerDAO.update(marker);
	}

	public List<Marker> getMarker(HashMap<String, Object> searchFields) {
		return markerDAO.get(searchFields);
	}

	public Marker deleteMarker(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(id != null) { map.put("primaryId", id); }
		Marker marker = markerDAO.get(map).get(0);
		return markerDAO.delete(marker);
	}

}

package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.MarkerDAO;
import org.jax.mgi.mgd.api.entities.Marker;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

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

	public SearchResults<Marker> getMarker(HashMap<String, Object> searchFields) {
		return markerDAO.search(searchFields);
	}

	public SearchResults<Marker> deleteMarker(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(id != null) { map.put("primaryId", id); }
		SearchResults<Marker> results = markerDAO.search(map);
		if (results.status_code != Constants.HTTP_OK) {
			return results;
		}
		return markerDAO.delete(results.items.get(0));
	}

}

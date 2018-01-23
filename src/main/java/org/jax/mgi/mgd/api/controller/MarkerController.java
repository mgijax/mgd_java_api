package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.rest.interfaces.MarkerRESTInterface;
import org.jax.mgi.mgd.api.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class MarkerController extends BaseController implements MarkerRESTInterface {

	@Inject
	private MarkerService markerService;
	
	@Override
	public Marker create(String api_access_token, Marker marker) {
		if(authenticate(api_access_token)) {
			return markerService.create(marker);
		} else {
			return null;
		}
	}

	@Override
	public Marker update(String api_access_token, Marker marker) {
		if(authenticate(api_access_token)) {
			return markerService.update(marker);
		} else {
			return null;
		}
	}

	@Override
	public Marker get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public Marker delete(String api_access_token, Integer key) {
		if(authenticate(api_access_token)) {
			return markerService.delete(key);
		} else {
			return null;
		}
	}

	@Override
	public SearchResults<Marker> search(Map<String, Object> postParams) {
		return markerService.search(postParams);
	}

}

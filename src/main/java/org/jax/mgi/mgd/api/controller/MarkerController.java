package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Marker;
import org.jax.mgi.mgd.api.rest.interfaces.MarkerRESTInterface;
import org.jax.mgi.mgd.api.service.MarkerService;
import org.jboss.logging.Logger;

public class MarkerController extends BaseController implements MarkerRESTInterface {

	@Inject
	private MarkerService markerService;
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public Marker createMarker(String api_access_token, Marker marker) {
		if(authenticate(api_access_token)) {
			return markerService.createMarker(marker);
		} else {
			return null;
		}
	}

	@Override
	public Marker updateMarker(String api_access_token, Marker marker) {
		if(authenticate(api_access_token)) {
			return markerService.updateMarker(marker);
		} else {
			return null;
		}
	}

	@Override
	public List<Marker> getMarker(String accid, String symbol) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(accid != null) { map.put("accid", accid); }
		if(symbol != null) { map.put("symbol", symbol); }
		log.info("Search Params: " + map);
		return markerService.getMarker(map);
	}

	@Override
	public Marker deleteMarker(String api_access_token, String id) {
		if(authenticate(api_access_token)) {
			return markerService.deleteMarker(id);
		} else {
			return null;
		}
	}


}

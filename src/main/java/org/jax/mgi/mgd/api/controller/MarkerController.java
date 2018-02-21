package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MarkerDomain;
import org.jax.mgi.mgd.api.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class MarkerController extends BaseController<MarkerDomain> {

	@Inject
	private MarkerService markerService;
	
	@Override
	public MarkerDomain create(MarkerDomain marker) {
		return markerService.create(marker);
	}

	@Override
	public MarkerDomain update(MarkerDomain marker) {
		return markerService.update(marker);
	}

	@Override
	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public MarkerDomain delete(Integer key) {
		return markerService.delete(key);
	}

	@Override
	public SearchResults<MarkerDomain> search(Map<String, Object> postParams) {
		return markerService.search(postParams);
	}

}

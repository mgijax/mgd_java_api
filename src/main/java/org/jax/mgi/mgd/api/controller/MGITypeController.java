package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.service.MGITypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class MGITypeController extends BaseController<MGITypeDomain> {

	@Inject
	private MGITypeService mgitypeService;

	@Override
	public MGITypeDomain create(MGITypeDomain mgitype) {
		return mgitypeService.create(mgitype);
	}

	@Override
	public MGITypeDomain get(Integer key) {
		return mgitypeService.get(key);
	}

	@Override
	public MGITypeDomain update(MGITypeDomain mgitype) {
		return mgitypeService.update(mgitype);
	}

	@Override
	public MGITypeDomain delete(Integer mgitype_key) {
		return mgitypeService.delete(mgitype_key);
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> postParams) {
		return mgitypeService.search(postParams);
	}
	
}

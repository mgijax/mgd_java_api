package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class OrganismController extends BaseController<OrganismDomain> {

	@Inject
	private OrganismService organismService;

	@Override
	public OrganismDomain create(OrganismDomain organism) {
		return organismService.create(organism);
	}

	@Override
	public OrganismDomain update(OrganismDomain organism) {
		return organismService.update(organism);
	}

	@Override
	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	@Override
	public OrganismDomain delete(Integer organism_key) {
		return organismService.delete(organism_key);
	}

	@Override
	public SearchResults<OrganismDomain> search(Map<String, Object> postParams) {
		return organismService.search(postParams);

	}

}

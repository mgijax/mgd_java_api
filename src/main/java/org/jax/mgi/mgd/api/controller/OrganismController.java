package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.rest.interfaces.OrganismRESTInterface;
import org.jax.mgi.mgd.api.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class OrganismController extends BaseController implements OrganismRESTInterface {

	@Inject
	private OrganismService organismService;

	@Override
	public OrganismDomain create(String api_access_token, OrganismDomain organism) {
		if(authenticate(api_access_token)) {
			return organismService.create(organism);
		}
		return null;
	}

	@Override
	public OrganismDomain update(String api_access_token, OrganismDomain organism) {
		if(authenticate(api_access_token)) {
			return organismService.update(organism);
		}
		return null;
	}

	@Override
	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	@Override
	public OrganismDomain delete(String api_access_token, Integer organism_key) {
		if(authenticate(api_access_token)) {
			return organismService.delete(organism_key);
		}
		return null;
	}

	@Override
	public SearchResults<OrganismDomain> search(Map<String, Object> postParams) {
		return organismService.search(postParams);

	}

}

package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Organism;
import org.jax.mgi.mgd.api.rest.interfaces.OrganismRESTInterface;
import org.jax.mgi.mgd.api.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class OrganismController extends BaseController implements OrganismRESTInterface {

	@Inject
	private OrganismService organismService;
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public Organism create(String api_access_token, Organism organism) {
		if(authenticate(api_access_token)) {
			return organismService.create(organism);
		}
		return null;
	}

	@Override
	public Organism update(String api_access_token, Organism organism) {
		if(authenticate(api_access_token)) {
			return organismService.update(organism);
		}
		return null;
	}

	@Override
	public Organism get(Integer key) {
		return organismService.get(key);
	}

	@Override
	public Organism delete(String api_access_token, Integer organism_key) {
		if(authenticate(api_access_token)) {
			return organismService.delete(organism_key);
		}
		return null;
	}

	@Override
	public SearchResults<Organism> search(Map<String, Object> postParams) {
		return organismService.search(postParams);

	}

}

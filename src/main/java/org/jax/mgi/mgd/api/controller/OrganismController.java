package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/organism")
@Api(value = "Organism Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganismController extends BaseController<OrganismDomain> {

	@Inject
	private OrganismService organismService;

	public OrganismDomain create(OrganismDomain organism) {
		return organismService.create(organism);
	}

	public OrganismDomain update(OrganismDomain organism) {
		return organismService.update(organism);
	}

	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	public OrganismDomain delete(Integer organism_key) {
		return organismService.delete(organism_key);
	}

	public SearchResults<OrganismDomain> search(Map<String, Object> postParams) {
		return organismService.search(postParams);
	}

}

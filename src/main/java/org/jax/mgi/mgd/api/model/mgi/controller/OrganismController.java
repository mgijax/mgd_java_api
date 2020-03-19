package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/organism")
@Api(value = "Organism Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganismController extends BaseController<OrganismDomain> {

	@Inject
	private OrganismService organismService;

	@Override
	public SearchResults<OrganismDomain> create(OrganismDomain organism, User user) {
		return organismService.create(organism, user);
	}

	@Override
	public SearchResults<OrganismDomain> update(OrganismDomain organism, User user) {
		return organismService.update(organism, user);
	}

	@Override
	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	@Override
	public SearchResults<OrganismDomain> delete(Integer key, User user) {
		return organismService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<OrganismDomain> search() {
		return organismService.search();
	}
	
	@POST
	@ApiOperation(value = "Search for Marker module")
	@Path("/searchMarker")
	public List<OrganismDomain> searchMarker() {
		return organismService.searchMarker();
	}
	
}

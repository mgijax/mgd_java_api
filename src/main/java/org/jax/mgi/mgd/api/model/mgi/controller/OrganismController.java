package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
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
	public SearchResults<OrganismDomain> create(OrganismDomain domain, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results = organismService.create(domain, user);
		results = organismService.getResults(Integer.valueOf(results.items.get(0).getOrganismKey()));
		return results;
	}

	@Override
	public SearchResults<OrganismDomain> update(OrganismDomain domain, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results = organismService.update(domain, user);
		results = organismService.getResults(Integer.valueOf(results.items.get(0).getOrganismKey()));
		return results;
	}

	@Override
	public SearchResults<OrganismDomain> delete(Integer key, User user) {
		return organismService.delete(key, user);
	}
	
	@Override
	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from prb_probe table")
	@Path("/getObjectCount")
	public SearchResults<OrganismDomain> getObjectCount() {
		return organismService.getObjectCount();
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<OrganismDomain> search() {
		return organismService.search();
	}

	@POST
	@ApiOperation(value = "Search for Allele Module/Driver Gene")
	@Path("/searchDriverGene")
	public List<OrganismDomain> searchDriverGene() {
		return organismService.searchDriverGene();
	}
	
	@POST
	@ApiOperation(value = "Search for Marker module")
	@Path("/searchMarker")
	public List<OrganismDomain> searchMarker() {
		return organismService.searchMarker();
	}

	@POST
	@ApiOperation(value = "Search for Probe module")
	@Path("/searchProbe")
	public List<OrganismDomain> searchProbe() {
		return organismService.searchProbe();
	}
	
	@POST
	@ApiOperation(value = "Search for organisms Antigen module")
	@Path("/searchAntigen")
	public List<OrganismDomain> searchAntigen() {
		return organismService.searchAntigen();
	}
	
	@POST
	@ApiOperation(value = "Search for organisms Antibody module")
	@Path("/searchAntibody")
	public List<OrganismDomain> searchAntibody() {
		return organismService.searchAntibody();
	}
	
	@POST
	@ApiOperation(value = "Search for organisms GXD/HT Sample module")
	@Path("/searchGXDHTSample")
	public List<SlimOrganismDomain> searchGXDHTSample() {
		return organismService.searchGXDHTSample();
	}	
}

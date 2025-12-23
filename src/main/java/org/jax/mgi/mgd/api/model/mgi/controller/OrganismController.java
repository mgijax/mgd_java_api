package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/organism")
@Tag(name = "Organism Endpoints")
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
	@Operation(description = "Get the object count from prb_probe table")
	@Path("/getObjectCount")
	public SearchResults<OrganismDomain> getObjectCount() {
		return organismService.getObjectCount();
	}

	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<OrganismDomain> search(OrganismDomain searchDomain) {
		return organismService.search(searchDomain);
	}

	@POST
	@Operation(description = "Search for Allele Module/Driver Gene")
	@Path("/searchDriverGene")
	public List<OrganismDomain> searchDriverGene() {
		return organismService.searchDriverGene();
	}
	
	@POST
	@Operation(description = "Search for Marker module")
	@Path("/searchMarker")
	public List<OrganismDomain> searchMarker() {
		return organismService.searchMarker();
	}

	@POST
	@Operation(description = "Search for Probe module")
	@Path("/searchProbe")
	public List<OrganismDomain> searchProbe() {
		return organismService.searchProbe();
	}
	
	@POST
	@Operation(description = "Search for Antigen organisms in Antibody module")
	@Path("/searchAntigen")
	public List<OrganismDomain> searchAntigen() {
		return organismService.searchAntigen();
	}
	
	@POST
	@Operation(description = "Search for Antibody organisms in Antibody module")
	@Path("/searchAntibody")
	public List<OrganismDomain> searchAntibody() {
		return organismService.searchAntibody();
	}
	
	@POST
	@Operation(description = "Search for organisms GXD/HT Sample module")
	@Path("/searchGXDHTSample")
	public List<SlimOrganismDomain> searchGXDHTSample() {
		return organismService.searchGXDHTSample();
	}	
	
	@POST
	@Operation(description = "Search for organisms Allele Relationship module")
	@Path("/searchAlleleRelationship")
	public List<SlimOrganismDomain> searchAlleleRelationship() {
		return organismService.searchAlleleRelationship();
	}	
}

package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeTissueDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeTissueService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/tissue")
@Tag(name = "Tissue Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeTissueController extends BaseController<ProbeTissueDomain> {

	@Inject
	private ProbeTissueService probeTissueService;
	
	@Override
	public SearchResults<ProbeTissueDomain> create(ProbeTissueDomain domain, User user) {
		log.info("ProbeTissueController create");
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results = probeTissueService.create(domain, user);
		results = probeTissueService.getResults(Integer.valueOf(results.items.get(0).getTissueKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeTissueDomain> update(ProbeTissueDomain domain, User user) {
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results = probeTissueService.update(domain, user);
		results = probeTissueService.getResults(Integer.valueOf(results.items.get(0).getTissueKey()));
		return results;
	}

	@Override
	public ProbeTissueDomain get(Integer key) {
		return probeTissueService.get(key);
	}

	@Override
	public SearchResults<ProbeTissueDomain> delete(Integer key, User user) {
		return probeTissueService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Validate Tissue")
	@Path("/validateTissue")
	public List<ProbeTissueDomain> validateStrain(ProbeTissueDomain searchDomain) {
	
		List<ProbeTissueDomain> results = new ArrayList<ProbeTissueDomain>();

		try {
			results = probeTissueService.validateTissue(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "get list of tissues")
	@Path("/getTissueList")
	public SearchResults<String> getTissueList() {
	
		SearchResults<String> results = null;

		try {
			results = probeTissueService.getTissueList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//log.info(results);
		return results;
	}
}

package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AllelePairService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/allelepair")
@Tag(name = "Allele Pair Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AllelePairController extends BaseController<AllelePairDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private AllelePairService allelePairService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<AllelePairDomain> create(AllelePairDomain domain, User user) {	
		return allelePairService.create(domain, user);
	}

	@Override
	public SearchResults<AllelePairDomain> update(AllelePairDomain domain, User user) {
		return allelePairService.update(domain, user);
	}

	@Override
	public AllelePairDomain get(Integer key) {
		return allelePairService.get(key);
	}

	@Override
	public SearchResults<AllelePairDomain> delete(Integer key, User user) {
		// this table contains a compound primary key
		// deletes to this table are implemented in the parent's "update" method
		return null;
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<AllelePairDomain> search(Integer key) {
		return allelePairService.search(key);
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<AllelePairDomain> domain, User user) {
		return allelePairService.process(parentKey, domain, user);
	}
	
	@POST
	@Operation(description = "Validate Allele Pair State/check result error")
	@Path("/validateAlleleState")
	public SearchResults<AllelePairDomain> validateAlleleState(List<AllelePairDomain> domain) {
		return allelePairService.validateAlleleState(domain);
	}	
	
	@POST
	@Operation(description = "Validate Mutant Cell Lines")
	@Path("/validateMutantCellLines")
	public SearchResults<AllelePairDomain> validateMutantCellLine(AllelePairDomain domain) {
		return allelePairService.validateMutantCellLines(domain);
	}
		
}

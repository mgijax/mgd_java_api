package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AllelePairService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelepair")
@Api(value = "Allele Pair Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<AllelePairDomain> search(Integer key) {
		return allelePairService.search(key);
	}
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<AllelePairDomain> domain, User user) {
		return allelePairService.process(parentKey, domain, user);
	}
	
	@POST
	@ApiOperation(value = "Validate Allele Pair State/check result error")
	@Path("/validateAlleleState")
	public SearchResults<AllelePairDomain> validateAlleleState(List<AllelePairDomain> domain) {
		return allelePairService.validateAlleleState(domain);
	}	
	
	@POST
	@ApiOperation(value = "Validate Mutant Cell Lines")
	@Path("/validateMutantCellLines")
	public SearchResults<AllelePairDomain> validateMutantCellLine(AllelePairDomain domain) {
		return allelePairService.validateMutantCellLines(domain);
	}
		
}

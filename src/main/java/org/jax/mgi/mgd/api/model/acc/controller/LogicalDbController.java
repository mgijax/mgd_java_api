package org.jax.mgi.mgd.api.model.acc.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDbDomain;
import org.jax.mgi.mgd.api.model.acc.service.LogicalDbService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/logicaldb")
@Tag(name = "Logical DB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogicalDbController extends BaseController<LogicalDbDomain> {

	@Inject
	private LogicalDbService ldbService;

	@Override
	public SearchResults<LogicalDbDomain> create(LogicalDbDomain domain, User user) {
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		results = ldbService.create(domain, user);
		results = ldbService.getResults(Integer.valueOf(results.items.get(0).getLogicalDBKey()));
		return results;
	}

	@Override
	public SearchResults<LogicalDbDomain> update(LogicalDbDomain domain, User user) {
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		results = ldbService.update(domain, user);
		results = ldbService.getResults(Integer.valueOf(results.items.get(0).getLogicalDBKey()));
		return results;	
	}
	
	@Override
	public LogicalDbDomain get(Integer key) {
		return ldbService.get(key);
	}

	@Override
	public SearchResults<LogicalDbDomain> delete(Integer key, User user) {
		return ldbService.delete(key, user);
	}

	@GET
	@Operation(description = "Get the object count from acc_logicalDB table")
	@Path("/getObjectCount")
	public SearchResults<LogicalDbDomain> getObjectCount() {
		return ldbService.getObjectCount();
	}

	@POST
	@Operation(description = "Search/returns LogicalDbDomain")
	@Path("/search")	
	public List<LogicalDbDomain> search(LogicalDbDomain searchDomain) {

		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();

		try {
			results = ldbService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;

	}

	@POST
	@Operation(description = "Get Mutant Cell Line Logical DBs")
	@Path("/searchMCLSet")
	public List<LogicalDbDomain> searchMCLSet() {
			
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		
		try {
			results = ldbService.searchMCLSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Get Probe Logical DBs")
	@Path("/searchProbeSet")
	public List<LogicalDbDomain> searchProbeSet() {
			
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		
		try {
			results = ldbService.searchProbeSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Get Probe Source Logical DBs")
	@Path("/searchProbeSourceSet")
	public List<LogicalDbDomain> searchProbeSourceSet() {
			
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		
		try {
			results = ldbService.searchProbeSourceSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Get Probe Strain Logical DBs")
	@Path("/searchProbeStrainSet")
	public List<LogicalDbDomain> searchProbeStrainSet() {
			
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		
		try {
			results = ldbService.searchProbeStrainSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}

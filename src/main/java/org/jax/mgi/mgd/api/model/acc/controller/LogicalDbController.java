package org.jax.mgi.mgd.api.model.acc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDbDomain;
import org.jax.mgi.mgd.api.model.acc.service.LogicalDbService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/logicaldb")
@Api(value = "Logical DB Endpoints")
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
	@ApiOperation(value = "Get the object count from acc_logicalDB table")
	@Path("/getObjectCount")
	public SearchResults<LogicalDbDomain> getObjectCount() {
		return ldbService.getObjectCount();
	}

	@POST
	@ApiOperation(value = "Search/returns LogicalDbDomain")
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
	@ApiOperation(value = "Get Mutant Cell Line Logical DBs")
	@Path("/mclLogicalDBs")
	public List<LogicalDbDomain> getMCLLogicalDBs() {
			
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		
		try {
			results = ldbService.getMCLLogicalDBs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}

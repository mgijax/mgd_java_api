package org.jax.mgi.mgd.api.model.dag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.service.DagNodeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/dagNode")
@Api(value = "DAG Node Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DagNodeController extends BaseController<DagNodeDomain> {

	@Inject
	private DagNodeService dagNodeService;

	@Override
	public SearchResults<DagNodeDomain> create(DagNodeDomain term, User user) {
		return dagNodeService.create(term, user);
	}

	@Override
	public SearchResults<DagNodeDomain> update(DagNodeDomain term, User user) {
		return dagNodeService.update(term, user);
	}

	@Override
	public DagNodeDomain get(Integer key) {
		return dagNodeService.get(key);
	}

	@Override
	public SearchResults<DagNodeDomain> delete(Integer key, User user) {
		return dagNodeService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<DagNodeDomain> search(DagNodeDomain searchDomain) {
			
		List<DagNodeDomain> results = new ArrayList<DagNodeDomain>();
		
		try {
			results = dagNodeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
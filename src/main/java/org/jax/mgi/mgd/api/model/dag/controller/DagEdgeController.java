package org.jax.mgi.mgd.api.model.dag.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.dag.domain.DagEdgeDomain;
import org.jax.mgi.mgd.api.model.dag.service.DagEdgeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dagEdge")
@Tag(name = "DAG Edge Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DagEdgeController extends BaseController<DagEdgeDomain> {

	@Inject
	private DagEdgeService dagEdgeService;

	@Override
	public SearchResults<DagEdgeDomain> create(DagEdgeDomain term, User user) {
		return dagEdgeService.create(term, user);
	}

	@Override
	public SearchResults<DagEdgeDomain> update(DagEdgeDomain term, User user) {
		return dagEdgeService.update(term, user);
	}

	@Override
	public DagEdgeDomain get(Integer key) {
		return dagEdgeService.get(key);
	}

	@Override
	public SearchResults<DagEdgeDomain> delete(Integer key, User user) {
		return dagEdgeService.delete(key, user);
	}

	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<DagEdgeDomain> search(DagEdgeDomain searchDomain) {
			
		List<DagEdgeDomain> results = new ArrayList<DagEdgeDomain>();
		
		try {
			results = dagEdgeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}

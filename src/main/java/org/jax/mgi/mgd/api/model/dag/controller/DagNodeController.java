package org.jax.mgi.mgd.api.model.dag.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.service.DagNodeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dagNode")
@Tag(name = "DAG Node Endpoints")
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
	@Operation(description = "Search")
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

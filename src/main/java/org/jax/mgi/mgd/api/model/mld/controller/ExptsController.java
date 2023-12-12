package org.jax.mgi.mgd.api.model.mld.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.domain.SlimExptsDomain;
import org.jax.mgi.mgd.api.model.mld.service.ExptsService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mapping")
@Tag(name = "Mapping Experiments Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExptsController extends BaseController<ExptsDomain> {

	@Inject
	private ExptsService exptsService;

	@Override
	public SearchResults<ExptsDomain> create(ExptsDomain domain, User user) {
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		results = exptsService.create(domain, user);
		results = exptsService.getResults(Integer.valueOf(results.items.get(0).getExptKey()));
		return results;
	}

	@Override
	public SearchResults<ExptsDomain> update(ExptsDomain domain, User user) {
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		results = exptsService.update(domain, user);
		results = exptsService.getResults(Integer.valueOf(results.items.get(0).getExptKey()));
		return results;
	}

	@Override
	public SearchResults<ExptsDomain> delete(Integer key, User user) {
		return exptsService.delete(key, user);
	}
	
	@Override
	public ExptsDomain get(Integer key) {
		return exptsService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from mld_expts table")
	@Path("/getObjectCount")
	public SearchResults<ExptsDomain> getObjectCount() {
		return exptsService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns domain")
	@Path("/search")
	public List<SlimExptsDomain> search(ExptsDomain searchDomain) {
	
		List<SlimExptsDomain> results = new ArrayList<SlimExptsDomain>();

		try {
			results = exptsService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	// ----------------------
	// get by marker

	@GET
	@Operation(description = "Get list of experiments domains by marker id")
	@Path("/getExptsByMarker")
	public SearchResults<SlimExptsDomain> getExptsByMarker(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SlimExptsDomain> results = new SearchResults<SlimExptsDomain>();

		try {
			results = exptsService.getExptsByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadExptsByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadExptsByMarker(@QueryParam("accid") String accid) {
             return exptsService.downloadExptsByMarker(accid);
	}

	// ----------------------
	// get by ref

	@GET
	@Operation(description = "Get list of experiments domains by jnum id")
	@Path("/getExptsByRef")
	public SearchResults<SlimExptsDomain> getExptsByRef(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SlimExptsDomain> results = new SearchResults<SlimExptsDomain>();

		try {
			results = exptsService.getExptsByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadExptsByRef")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadExptsByRef(@QueryParam("accid") String accid) {
             return exptsService.downloadExptsByRef(accid);
	}
}

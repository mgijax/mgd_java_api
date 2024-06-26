package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryGXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GXDIndexService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/gxdindex")
@Tag(name = "GXD Index Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GXDIndexController extends BaseController<GXDIndexDomain> {

	@Inject
	private GXDIndexService indexService;

	@Override
	public SearchResults<GXDIndexDomain> create(GXDIndexDomain domain, User user) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results = indexService.create(domain, user);
		results = indexService.getResults(Integer.valueOf(results.items.get(0).getIndexKey()));
		return results;
	}

	@Override
	public SearchResults<GXDIndexDomain> update(GXDIndexDomain domain, User user) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results = indexService.update(domain, user);
		results = indexService.getResults(Integer.valueOf(results.items.get(0).getIndexKey()));
		return results;
	}

	@Override
	public SearchResults<GXDIndexDomain> delete(Integer key, User user) {
		return indexService.delete(key, user);
	}
	
	@Override
	public GXDIndexDomain get(Integer key) {
		return indexService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from gxd_index table")
	@Path("/getObjectCount")
	public SearchResults<GXDIndexDomain> getObjectCount() {
		return indexService.getObjectCount();
	}
	
	@POST
	@Operation(description = "Search/returns Index domain")
	@Path("/search")
	public List<SlimGXDIndexDomain> search(GXDIndexDomain searchDomain) {
	
		List<SlimGXDIndexDomain> results = new ArrayList<SlimGXDIndexDomain>();

		try {
			results = indexService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get list of index domains by marker accession id")
	@Path("/getIndexByMarker")
	public List<SummaryGXDIndexDomain> getProbeByMarker(String accid) {
		
		List<SummaryGXDIndexDomain> results = new ArrayList<SummaryGXDIndexDomain>();

		try {
			results = indexService.getIndexByMarker(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get list of index domains by reference jnumid")
	@Path("/getIndexByRef")
	public List<SummaryGXDIndexDomain> getProbeByRef(String jnumid) {
		
		List<SummaryGXDIndexDomain> results = new ArrayList<SummaryGXDIndexDomain>();

		try {
			results = indexService.getIndexByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}

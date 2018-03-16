package org.jax.mgi.mgd.api.model;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public interface BaseSearchInterface<D, F> {

	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<D> search(
		@ApiParam(value = "Key Value pairs for the search fields of this object")
		F searchForm
	);
	
}

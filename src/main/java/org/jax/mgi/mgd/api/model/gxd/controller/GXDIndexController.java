package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GXDIndexService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/gxdindex")
@Api(value = "GXD Index Endpoints")
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
	
}

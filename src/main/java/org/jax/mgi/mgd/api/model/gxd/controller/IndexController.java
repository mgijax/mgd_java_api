package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.gxd.search.IndexSearchForm;
import org.jax.mgi.mgd.api.model.gxd.service.IndexService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/index")
@Api(value = "Index Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndexController extends BaseController<IndexDomain> {

	@Inject
	private IndexService indexService;

	@Override
	public SearchResults<IndexDomain> create(IndexDomain index, User user) {
		return indexService.create(index, user);
	}

	@Override
	public SearchResults<IndexDomain> update(IndexDomain index, User user) {
		return indexService.update(index, user);
	}

	@Override
	public IndexDomain get(Integer indexKey) {
		return indexService.get(indexKey);
	}

	@Override
	public SearchResults<IndexDomain> delete(Integer key, User user) {
		return indexService.delete(key, user);
	}
	
}

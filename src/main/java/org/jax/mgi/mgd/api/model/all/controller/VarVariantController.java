package org.jax.mgi.mgd.api.model.all.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.all.domain.VarVariantDomain;
import org.jax.mgi.mgd.api.model.all.service.VarVariantService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allel.alliant")
@Api(value = "Allele Variant Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VarVariantController extends BaseController<VarVariantDomain> {

	@Inject
	private VarVariantService.alliantService;

	@Override
	public SearchResults<VarVariantDomain> create(VarVariantDomain event, User user) {
		return.alliantService.create(event, user);
	}

	@Override
	public SearchResults<VarVariantDomain> update(VarVariantDomain event, User user) {
		return.alliantService.update(event, user);
	}

	@Override
	public VarVariantDomain get(Integer key) {
		return.alliantService.get(key);
	}

	@Override
	public SearchResults<VarVariantDomain> delete(Integer key, User user) {
		return.alliantService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<VarVariantDomain> search() {
		return.alliantService.search();
	}
	
}

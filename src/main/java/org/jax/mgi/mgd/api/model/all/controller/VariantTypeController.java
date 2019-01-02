package org.jax.mgi.mgd.api.model.all.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.VariantTypeDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleVariantService;
import org.jax.mgi.mgd.api.model.all.service.VariantTypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelevarianttype")
@Api(value = "Allele Variant Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VariantTypeController extends BaseController<VariantTypeDomain> {

	@Inject
	private VariantTypeService variantTypeService;

	@Override
	public SearchResults<VariantTypeDomain> create(VariantTypeDomain domain, User user) {
		
		SearchResults<VariantTypeDomain> results = new SearchResults<VariantTypeDomain>();
		try
		{
			results = variantTypeService.create(domain, user);
			results = variantTypeService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
		} catch (Exception e) {
			results.setError("Failed : create", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;
	}

	@Override
	public SearchResults<VariantTypeDomain> update(VariantTypeDomain domain, User user) {
		return variantTypeService.update(domain, user);
	}

	@Override
	public VariantTypeDomain get(Integer key) {
		return variantTypeService.get(key);
	}

	@Override
	public SearchResults<VariantTypeDomain> delete(Integer key, User user) {
		return variantTypeService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<VariantTypeDomain> search() {
		return variantTypeService.search();
	}
	
}

package org.jax.mgi.mgd.api.model.all.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.VariantEffectDomain;
import org.jax.mgi.mgd.api.model.all.service.VariantEffectService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelevarianteffect")
@Api(value = "Allele Variant Effect Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VariantEffectController extends BaseController<VariantEffectDomain> {

	@Inject
	private VariantEffectService variantEffectService;

	@Override
	public SearchResults<VariantEffectDomain> create(VariantEffectDomain domain, User user) {
		
		SearchResults<VariantEffectDomain> results = new SearchResults<VariantEffectDomain>();
		try
		{
			results = variantEffectService.create(domain, user);
			results = variantEffectService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
		} catch (Exception e) {
			results.setError("Failed : create", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;
	}

	@Override
	public SearchResults<VariantEffectDomain> update(VariantEffectDomain domain, User user) {
		return variantEffectService.update(domain, user);
	}

	@Override
	public VariantEffectDomain get(Integer key) {
		return variantEffectService.get(key);
	}

	@Override
	public SearchResults<VariantEffectDomain> delete(Integer key, User user) {
		return variantEffectService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<VariantEffectDomain> search() {
		return variantEffectService.search();
	}
	
}

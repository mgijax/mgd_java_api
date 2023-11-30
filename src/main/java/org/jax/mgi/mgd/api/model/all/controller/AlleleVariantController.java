package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleVariantService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelevariant")
@Api(value = "Allele Variant Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleVariantController extends BaseController<AlleleVariantDomain> {

	@Inject
	private AlleleVariantService variantService;

	@Override
	public SearchResults<AlleleVariantDomain> create(AlleleVariantDomain domain, User user) {
		
		SearchResults<AlleleVariantDomain> results = variantService.create(domain, user);
		return variantService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
		
	}

	@Override
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain domain, User user) {
		
		SearchResults<AlleleVariantDomain> results = variantService.update(domain, user);
		return variantService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
			
	}

	@Override
	public AlleleVariantDomain get(Integer key) {
		return variantService.get(key);
	}

	@Override
	public SearchResults<AlleleVariantDomain> delete(Integer key, User user) {
		
		return variantService.delete(key, user);
						
	}
	
	@GET
	@ApiOperation(value = "Get the object count from all_variant table")
	@Path("/getObjectCount")
	public SearchResults<AlleleVariantDomain> getObjectCount() {
		return variantService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<SlimAlleleVariantDomain> search(AlleleVariantDomain searchDomain) {
		return variantService.search(searchDomain);
	}

	@POST
	@ApiOperation(value = "Get SlimAlleleVariant by Allele")
	@Path("/getSlimByAllele")
	public List<SlimAlleleVariantDomain> getSlimByAllele(Integer key) {
			
		List<SlimAlleleVariantDomain> results = new ArrayList<SlimAlleleVariantDomain>();
		
		try {
			results = variantService.getSlimByAllele(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get All Variants per Allele")
	@Path("/getByAllele")
	public List<AlleleVariantDomain> getByAllele(Integer key) {
			
		List<AlleleVariantDomain> results = new ArrayList<AlleleVariantDomain>();
		
		try {
			results = variantService.getByAllele(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get HGVS string by chr")
	@Path("/getHGVSByChr")
	public List<String> getHGVSByChr(String chrInfo) {
	
		List<String> results = new ArrayList<String>();

		try {
			results = variantService.getHGVSByChr(chrInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}

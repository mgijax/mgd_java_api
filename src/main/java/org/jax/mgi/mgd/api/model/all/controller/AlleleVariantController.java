package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleVariantService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/allelevariant")
@Tag(name = "Allele Variant Endpoints")
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
	@Operation(description = "Get the object count from all_variant table")
	@Path("/getObjectCount")
	public SearchResults<AlleleVariantDomain> getObjectCount() {
		return variantService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<SlimAlleleVariantDomain> search(AlleleVariantDomain searchDomain) {
		return variantService.search(searchDomain);
	}

	@POST
	@Operation(description = "Get SlimAlleleVariant by Allele")
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
	@Operation(description = "Get All Variants per Allele")
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
	@Operation(description = "Get HGVS by proper input format")
	@Path("/getHGVS")
	public List<String> getHGVS(String searchHGVS) {
	
		List<String> results = new ArrayList<String>();

		try {
			results = variantService.getHGVS(searchHGVS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}

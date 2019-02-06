package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleVariantService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelevariant")
@Api(value = "Allele Variant Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleVariantController extends BaseController<AlleleVariantDomain> {

	ObjectMapper mapper = new ObjectMapper();
	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleVariantService variantService;

	@Override
	public SearchResults<AlleleVariantDomain> create(AlleleVariantDomain domain, User user) {
		
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		
		try {
			log.info("create/before/json");
			log.info(mapper.writeValueAsString(domain));		
		} catch (Exception e) {	
			results.setError("Failed/create/before/json/incorrect format", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;	
		}
		
		try {
			results = variantService.create(domain, user);
			results = variantService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
			log.info("create/after/results");
			log.info(mapper.writeValueAsString(results.items.get(0)));
		} catch (Exception e) {
			results.setError("Failed : create", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;
	}

	@Override
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain domain, User user) {
		
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		
		try {
			log.info("update/before/json");
			log.info(mapper.writeValueAsString(domain));		
		} catch (Exception e) {	
			results.setError("Failed/update/before/json/incorrect format", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;	
		}
		try {
			results = variantService.update(domain, user);
			results = variantService.getResults(Integer.valueOf(results.items.get(0).getVariantKey()));
			log.info("update/after/results");
			log.info(mapper.writeValueAsString(results.items.get(0)));
		} catch (Exception e) {
			results.setError("Failed : update", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;		
	}

	@Override
	public AlleleVariantDomain get(Integer key) {
		return variantService.get(key);
	}

	@Override
	public SearchResults<AlleleVariantDomain> delete(Integer key, User user) {
		
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		
		try {
			log.info("delete/before/json");
			log.info(mapper.writeValueAsString(key));		
		} catch (Exception e) {	
			results.setError("Failed/delete/before/json/incorrect format", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;	
		}
		try {
			results = variantService.delete(key, user);
			log.info("delete/after/results");
			log.info(mapper.writeValueAsString(results.items.get(0)));
		} catch (Exception e) {
			results.setError("Failed : delete", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;			
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
		
}

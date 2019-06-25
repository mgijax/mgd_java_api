package org.jax.mgi.mgd.api.model.gxd.controller;

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
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GenotypeMPService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mpannot")
@Api(value = "Genotype MP Annotations Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenotypeMPController extends BaseController<GenotypeMPDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private GenotypeMPService genotypeMPService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<GenotypeMPDomain> create(GenotypeMPDomain domain, User user) {	
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results = genotypeMPService.create(domain, user);
		results = genotypeMPService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;
	}

	@Override
	public SearchResults<GenotypeMPDomain> update(GenotypeMPDomain domain, User user) {
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results = genotypeMPService.update(domain, user);
		results = genotypeMPService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;	
	}

	@Override
	public GenotypeMPDomain get(Integer genotypeKey) {
		return genotypeMPService.get(genotypeKey);
	}

	@Override
	public SearchResults<GenotypeMPDomain> delete(Integer key, User user) {
		return genotypeMPService.delete(key, user);
	}

	@GET
	@ApiOperation(value = "Get the object count from voc_annot table where _annottype_key = 1002")
	@Path("/getObjectCount")
	public String getObjectCount() {
		return genotypeMPService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim genotype domain")
	@Path("/search")
	public List<SlimGenotypeDomain> search(GenotypeMPDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = genotypeMPService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}

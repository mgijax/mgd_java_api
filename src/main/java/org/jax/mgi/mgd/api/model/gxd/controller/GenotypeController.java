package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDataSetDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GenotypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/genotype")
@Api(value = "Genotype Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenotypeController extends BaseController<GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private GenotypeService genotypeService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<GenotypeDomain> create(GenotypeDomain domain, User user) {	
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results = genotypeService.create(domain, user);
		results = genotypeService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;
	}

	@Override
	public SearchResults<GenotypeDomain> update(GenotypeDomain domain, User user) {
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results = genotypeService.update(domain, user);
		results = genotypeService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;	
	}

	@Override
	public GenotypeDomain get(Integer genotypeKey) {
		return genotypeService.get(genotypeKey);
	}

	@Override
	public SearchResults<GenotypeDomain> delete(Integer key, User user) {
		return genotypeService.delete(key, user);
	}

	@GET
	@ApiOperation(value = "Get the object count from gxd_genotype table")
	@Path("/getObjectCount")
	public SearchResults<GenotypeDomain> getObjectCount() {
		return genotypeService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim genotype domain")
	@Path("/search")
	public List<SlimGenotypeDomain> search(GenotypeDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = genotypeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Get genotype data sets by genotype key")
	@Path("/getDataSets/{key}")
	public List<GenotypeDataSetDomain> getDataSet(@PathParam("key") Integer key) {
		return genotypeService.getDataSets(key);
	}
	
	@POST
	@ApiOperation(value = "Search data sets by jnum key")
	@Path("/searchDataSets/{key}")
	public List<SlimGenotypeDomain> searchDataSet(@PathParam("key") Integer key) {
		return genotypeService.searchDataSets(key);
	}
	
}

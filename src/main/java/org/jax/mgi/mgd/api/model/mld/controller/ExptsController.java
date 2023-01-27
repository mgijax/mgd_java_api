package org.jax.mgi.mgd.api.model.mld.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.domain.SlimExptsDomain;
import org.jax.mgi.mgd.api.model.mld.service.ExptsService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mapping")
@Api(value = "Mapping Experiments Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExptsController extends BaseController<ExptsDomain> {

	@Inject
	private ExptsService exptsService;

	@Override
	public SearchResults<ExptsDomain> create(ExptsDomain domain, User user) {
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		results = exptsService.create(domain, user);
		results = exptsService.getResults(Integer.valueOf(results.items.get(0).getExptKey()));
		return results;
	}

	@Override
	public SearchResults<ExptsDomain> update(ExptsDomain domain, User user) {
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		results = exptsService.update(domain, user);
		results = exptsService.getResults(Integer.valueOf(results.items.get(0).getExptKey()));
		return results;
	}

	@Override
	public SearchResults<ExptsDomain> delete(Integer key, User user) {
		return exptsService.delete(key, user);
	}
	
	@Override
	public ExptsDomain get(Integer key) {
		return exptsService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from mld_expts table")
	@Path("/getObjectCount")
	public SearchResults<ExptsDomain> getObjectCount() {
		return exptsService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<SlimExptsDomain> search(ExptsDomain searchDomain) {
	
		List<SlimExptsDomain> results = new ArrayList<SlimExptsDomain>();

		try {
			results = exptsService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@ApiOperation(value = "Get list of experiments domains by marker id")
	@Path("/getExptsByMarker")
	public SearchResults<SlimExptsDomain> getExptsByMarker(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SlimExptsDomain> results = new SearchResults<SlimExptsDomain>();

		try {
			results = exptsService.getExptsByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@GET
	@ApiOperation(value = "Get list of experiments domains by jnum id")
	@Path("/getExptsByRef")
	public SearchResults<SlimExptsDomain> getExptsByRef(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SlimExptsDomain> results = new SearchResults<SlimExptsDomain>();

		try {
			results = exptsService.getExptsByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
    protected String formatTsv (String endpoint, Object obj) {   
    	if (endpoint.startsWith("getExptsBy")) {
            String[][] cols = {
                {"Assay ID",        "jnumid"},
                {"Marker Symbol",   "markerSymbol"},
                {"Assay Type",      "assayType"},
                {"Specimen Label",  "specimenLabel"},
                {"Age",             "age"},
                {"Age Note",        "ageNote"},
                {"Sex",             "sex"},
                {"Hybridization",   "hybridization"},
                {"Fixation",        "fixationMethod"},
                {"Embedding",       "embeddingMethod"},
                {"Background",      "genotypeBackground"},
                {"Allele(s)",       "alleleDetailNote"},
                {"Specimen Note",   "specimenNote"}
                };
            return formatTsvHelper(obj, cols);
        } else {
            return null;
        } 	
    }	
}

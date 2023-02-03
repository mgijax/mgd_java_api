package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummarySpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.SpecimenService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/specimen")
@Api(value = "Specimen Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecimenController extends BaseController<SpecimenDomain> {

	@Inject
	private SpecimenService specimenService;

	@Override
	public SearchResults<SpecimenDomain> create(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.create(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> update(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.update(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		return specimenService.delete(key, user);
	}
	
	@Override
	public SpecimenDomain get(Integer key) {
		return specimenService.get(key);
	}
	
	@GET
	@ApiOperation(value = "Get list of specimen domains by reference jnum id")
	@Path("/getSpecimenByRef")
	public SearchResults<SummarySpecimenDomain> getSpecimenByRef(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SummarySpecimenDomain> results = new SearchResults<SummarySpecimenDomain>();

		try {
			results = specimenService.getSpecimenByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	/*
    protected String formatTsv (String endpoint, Object obj) {   
    	if (endpoint.startsWith("getSpecimenBy")) {
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
    */
}

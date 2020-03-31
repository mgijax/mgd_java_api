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
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleCellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/cellline")
@Api(value = "Allele Cell Line Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleCellLineController extends BaseController<AlleleCellLineDomain> {

	@Inject
	private AlleleCellLineService cellLineService;

	@Override
	public SearchResults<AlleleCellLineDomain> create(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results = cellLineService.create(domain, user);
		results = cellLineService.getResults(Integer.valueOf(results.items.get(0).getMutantCellLineKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDomain> update(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results = cellLineService.update(domain, user);
		results = cellLineService.getResults(Integer.valueOf(results.items.get(0).getMutantCellLineKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDomain> delete(Integer key, User user) {
		return cellLineService.delete(key, user);
	}
	
	@Override
	public AlleleCellLineDomain get(Integer key) {
		return cellLineService.get(key);
	}

	@POST
	@ApiOperation(value = "Validate Allele Mutant Cell Line")
	@Path("/validateMutantCellLine")
	public List<AlleleCellLineDomain> validateAllele(AlleleCellLineDomain searchDomain) {
	
		List<AlleleCellLineDomain> results = new ArrayList<AlleleCellLineDomain>();

		try {
			results = cellLineService.validateMutantCellLine(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}

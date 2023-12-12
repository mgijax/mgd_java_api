package org.jax.mgi.mgd.api.model.all.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleCellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/allelecellline")
@Tag(name = "Allele Cell Line Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleCellLineController extends BaseController<AlleleCellLineDomain> {

	@Inject
	private AlleleCellLineService alleleCellLineService;

	@Override
	public SearchResults<AlleleCellLineDomain> create(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results = alleleCellLineService.create(domain, user);
		results = alleleCellLineService.getResults(Integer.valueOf(results.items.get(0).getAssocKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDomain> update(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results = alleleCellLineService.update(domain, user);
		results = alleleCellLineService.getResults(Integer.valueOf(results.items.get(0).getAssocKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDomain> delete(Integer key, User user) {
		return alleleCellLineService.delete(key, user);
	}
	
	@Override
	public AlleleCellLineDomain get(Integer key) {
		return alleleCellLineService.get(key);
	}
	
}

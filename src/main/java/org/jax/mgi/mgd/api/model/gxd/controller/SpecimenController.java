package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.search.SpecimenSearchForm;
import org.jax.mgi.mgd.api.model.gxd.service.SpecimenService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/specimen")
@Api(value = "Specimen Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecimenController extends BaseController<SpecimenDomain> {

	@Inject
	private SpecimenService specimenService;

	public SearchResults<SpecimenDomain> create(SpecimenDomain specimen, User user) {
		return specimenService.create(specimen, user);
	}

	public SearchResults<SpecimenDomain> update(SpecimenDomain specimen, User user) {
		return specimenService.update(specimen, user);
	}

	public SpecimenDomain get(Integer specimenKey) {
		return specimenService.get(specimenKey);
	}

	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		return specimenService.delete(key, user);
	}
	
}

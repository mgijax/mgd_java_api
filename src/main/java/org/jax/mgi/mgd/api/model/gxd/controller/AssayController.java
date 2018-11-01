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
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.search.AssaySearchForm;
import org.jax.mgi.mgd.api.model.gxd.service.AssayService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/assay")
@Api(value = "Assay Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssayController extends BaseController<AssayDomain> {

	@Inject
	private AssayService assayService;

	@Override
	public SearchResults<AssayDomain> create(AssayDomain assay, User user) {
		return assayService.create(assay, user);
	}

	@Override
	public SearchResults<AssayDomain> update(AssayDomain assay, User user) {
		return assayService.update(assay, user);
	}

	@Override
	public AssayDomain get(Integer assayKey) {
		return assayService.get(assayKey);
	}

	@Override
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		return assayService.delete(key, user);
	}
	
}

package org.jax.mgi.mgd.api.model.bib.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.search.ReferenceSearchForm;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/reference")
@Api(value = "Reference Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReferenceController extends BaseController<ReferenceDomain> implements BaseSearchInterface<ReferenceDomain, ReferenceSearchForm> {

	@Inject
	private ReferenceService referenceService;

	public ReferenceDomain create(ReferenceDomain reference, User user) {
		try {
			return referenceService.create(reference, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReferenceDomain update(ReferenceDomain reference, User user) {
		return referenceService.update(reference, user);
	}

	public ReferenceDomain get(Integer referenceKey) {
		return referenceService.get(referenceKey);
	}

	public ReferenceDomain delete(Integer key, User user) {
		return referenceService.delete(key, user);
	}
	
	@Override
	public SearchResults<ReferenceDomain> search(ReferenceSearchForm searchForm) {
		return referenceService.search(searchForm);
	}
}

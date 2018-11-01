package org.jax.mgi.mgd.api.model.voc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.search.TermSearchForm;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/term")
@Api(value = "Term Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TermController extends BaseController<TermDomain> implements BaseSearchInterface<TermDomain, TermSearchForm> {

	@Inject
	private TermService termService;

	public SearchResults<TermDomain> create(TermDomain term, User user) {
		return null;
	}

	public SearchResults<TermDomain> update(TermDomain term, User user) {
		return termService.update(term, user);
	}

	public TermDomain get(Integer key) {
		return termService.get(key);
	}

	public SearchResults<TermDomain> delete(Integer key, User user) {
		return termService.delete(key, user);
	}

	@Override
	public SearchResults<TermDomain> search(TermSearchForm searchForm) {
		return termService.search(searchForm);
	}
	
}

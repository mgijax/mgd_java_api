package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/term")
@Api(value = "Term Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TermController extends BaseController<TermDomain> {

	@Inject
	private TermService termService;

	public TermDomain create(TermDomain term) {
		return termService.create(term);
	}

	public TermDomain update(TermDomain term) {
		return termService.update(term);
	}

	public TermDomain get(Integer key) {
		return termService.get(key);
	}

	public TermDomain delete(Integer term_key) {
		return termService.delete(term_key);
	}

	public SearchResults<TermDomain> search(Map<String, Object> postParams) {
		return termService.search(postParams, "sequenceNum");
	}
	
}

package org.jax.mgi.mgd.api.model.mgi.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISetMemberService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgisetmember")
@Tag(name = "MGI Set Member Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISetMemberController extends BaseController<MGISetMemberDomain> {

	@Inject
	private MGISetMemberService setMemberService;

	@Override
	public SearchResults<MGISetMemberDomain> create(MGISetMemberDomain domain, User user) {
		return setMemberService.create(domain, user);
	}

	@Override
	public SearchResults<MGISetMemberDomain> update(MGISetMemberDomain domain, User user) {
		return setMemberService.update(domain, user);
	}

	@Override
	public MGISetMemberDomain get(Integer key) {
		return setMemberService.get(key);
	}

	@Override
	public SearchResults<MGISetMemberDomain> delete(Integer key, User user) {
		return setMemberService.delete(key, user);
	}
	
}

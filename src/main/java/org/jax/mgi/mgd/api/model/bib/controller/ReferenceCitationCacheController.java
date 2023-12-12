package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceCitationCacheService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/referencecitation")
@Tag(name = "Reference Citation Cache Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReferenceCitationCacheController extends BaseController<ReferenceCitationCacheDomain> {

	@Inject
	private ReferenceCitationCacheService referenceService;

	@Override
	public SearchResults<ReferenceCitationCacheDomain> create(ReferenceCitationCacheDomain domain, User user) {
		return referenceService.create(domain, user);
	}

	@Override
	public SearchResults<ReferenceCitationCacheDomain> update(ReferenceCitationCacheDomain domain, User user) {
		return referenceService.update(domain, user);
	}

	@Override
	public ReferenceCitationCacheDomain get(Integer key) {
		return referenceService.get(key);
	}

	@Override
	public SearchResults<ReferenceCitationCacheDomain> delete(Integer key, User user) {
		return referenceService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<ReferenceCitationCacheDomain> search(ReferenceCitationCacheDomain searchDomain) {	
		List<ReferenceCitationCacheDomain> results = new ArrayList<ReferenceCitationCacheDomain>();
		try {
			results = referenceService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

}

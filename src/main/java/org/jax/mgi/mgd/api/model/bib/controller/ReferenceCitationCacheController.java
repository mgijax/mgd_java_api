package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceCitationCacheService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/referencecitation")
@Api(value = "Reference Citation Cache Endpoints")
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
	@ApiOperation(value = "Search")
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

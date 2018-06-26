package org.jax.mgi.mgd.api.model.seq.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.domain.SeqMarkerCacheDomain;
import org.jax.mgi.mgd.api.model.seq.search.SeqMarkerCacheSearchForm;
import org.jax.mgi.mgd.api.model.seq.service.SeqMarkerCacheService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/seqmarkercache")
@Api(value = "SeqMarkerCache Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeqMarkerCacheController extends BaseController<SeqMarkerCacheDomain> implements BaseSearchInterface<SeqMarkerCacheDomain, SeqMarkerCacheSearchForm> {

	@Inject
	private SeqMarkerCacheService seqmarkercacheService;

	@Override
	public SeqMarkerCacheDomain create(SeqMarkerCacheDomain seqmarkercache, User user) {
		try {
			return seqmarkercacheService.create(seqmarkercache, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SeqMarkerCacheDomain update(SeqMarkerCacheDomain seqmarkercache, User user) {
		return seqmarkercacheService.update(seqmarkercache, user);
	}

	@Override
	public SeqMarkerCacheDomain get(Integer seqmarkercacheKey) {
		return seqmarkercacheService.get(seqmarkercacheKey);
	}

	@Override
	public SeqMarkerCacheDomain delete(Integer key, User user) {
		return seqmarkercacheService.delete(key, user);
	}
	
	@Override
	public SearchResults<SeqMarkerCacheDomain> search(SeqMarkerCacheSearchForm searchForm) {
		return seqmarkercacheService.search(searchForm);
	}

}

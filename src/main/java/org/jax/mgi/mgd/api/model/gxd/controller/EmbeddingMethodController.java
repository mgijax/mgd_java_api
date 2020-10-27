package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.EmbeddingMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.service.EmbeddingMethodService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gxdembedding")
@Api(value = "GXD EmbeddingMethod Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmbeddingMethodController extends BaseController<EmbeddingMethodDomain> {

	@Inject
	private EmbeddingMethodService embeddingService;

	@Override
	public SearchResults<EmbeddingMethodDomain> create(EmbeddingMethodDomain domain, User user) {
		SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();
		results = embeddingService.create(domain, user);
		results = embeddingService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<EmbeddingMethodDomain> update(EmbeddingMethodDomain domain, User user) {
		SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();
		results = embeddingService.update(domain, user);
		results = embeddingService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<EmbeddingMethodDomain> delete(Integer key, User user) {
		return embeddingService.delete(key, user);
	}
	
	@Override
	public EmbeddingMethodDomain get(Integer key) {
		return embeddingService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<EmbeddingMethodDomain> search(EmbeddingMethodDomain searchDomain) {
	
		List<EmbeddingMethodDomain> results = new ArrayList<EmbeddingMethodDomain>();

		try {
			results = embeddingService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}

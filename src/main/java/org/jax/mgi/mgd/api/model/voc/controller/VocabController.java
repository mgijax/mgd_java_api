package org.jax.mgi.mgd.api.model.voc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/vocab")
@Api(value = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VocabController extends BaseController<VocabularyDomain> {

	@Inject
	private VocabService vocabService;

	@Override
	public SearchResults<VocabularyDomain> create(VocabularyDomain object, User user) {
		return vocabService.create(object, user);
	}

	@Override
	public SearchResults<VocabularyDomain> update(VocabularyDomain object, User user) {
		return vocabService.update(object, user);
	}

	@Override
	public VocabularyDomain get(Integer key) {
		return vocabService.get(key);
	}

	@Override
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		return vocabService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search by vocab key or name")
	@Path("/search")	
	public SearchResults<SlimVocabularyDomain> search(SlimVocabularyDomain searchDomain) {
		return vocabService.search(searchDomain);
	}
	
}

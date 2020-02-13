package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyTermDomain;
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
	public SearchResults<VocabularyDomain> create(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results = vocabService.create(domain, user);
        results = vocabService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));

		return results;
	}

	@Override
	public SearchResults<VocabularyDomain> update(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results = vocabService.update(domain, user);
        results = vocabService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
        
        return results;
	}

	@Override
	public VocabularyDomain get(Integer key) {
		return vocabService.get(key);
	}
	
	@GET
	@ApiOperation(value = "Get the object count from voc_vocab table")
	@Path("/getObjectCount")
	public SearchResults<VocabularyDomain> getObjectCount() {
		return vocabService.getObjectCount();
	}
	
	@Override
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		return vocabService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search by vocab key or name")
	@Path("/search")	
	public SearchResults<SlimVocabularyTermDomain> search(SlimVocabularyTermDomain searchDomain) {
		return vocabService.search(searchDomain);
	}
	@GET
	@ApiOperation(value = "return all simple vocabs")
	@Path("/searchsimple")	
	public List<SlimVocabularyDomain> searchSimple() {
		return vocabService.searchSimple();
	}
	
}

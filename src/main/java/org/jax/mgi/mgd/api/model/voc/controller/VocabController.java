package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vocab")
@Tag(name = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VocabController extends BaseController<VocabularyDomain> {
	
	@Inject
	private VocabService vocabService;

	@Override
	public SearchResults<VocabularyDomain> create(VocabularyDomain domain, User user) {
		log.info("VocabController.create");
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results = vocabService.create(domain, user);
        results = vocabService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<VocabularyDomain> update(VocabularyDomain domain, User user) {
		log.info("VocabController.update");
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
	@Operation(description = "Get the object count from voc_vocab table")
	@Path("/getObjectCount")
	public SearchResults<VocabularyDomain> getObjectCount() {
		return vocabService.getObjectCount();
	}
	
	@Override
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		return vocabService.delete(key, user);
	}

	@POST
	@Operation(description = "Search by vocab key or name")
	@Path("/search")	
	public SearchResults<SlimVocabularyTermDomain> search(SlimVocabularyTermDomain searchDomain) {
		return vocabService.search(searchDomain);
	}
	
	@GET
	@Operation(description = "return all simple vocabs")
	@Path("/searchsimple")	
	public List<SlimVocabularyDomain> searchSimple() {
		return vocabService.searchSimple();
	}
	
	@POST
	@Operation(description = "Search by vocab key or name")
	@Path("/getVocabList")	
	public SearchResults<String> getVocabList(SlimVocabularyTermDomain searchDomain) {
		return vocabService.getVocabList(searchDomain);
	}
	
}

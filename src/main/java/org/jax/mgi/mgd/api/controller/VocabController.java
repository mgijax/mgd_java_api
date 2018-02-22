package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/vocab")
@Api(value = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VocabController extends BaseController<VocabularyDomain> {

	@Inject
	private VocabService vocabService;

	public VocabularyDomain create(VocabularyDomain object) {
		return vocabService.create(object);
	}

	public VocabularyDomain getByKey(Integer key) {
		return vocabService.get(key);
	}

	public VocabularyDomain update(VocabularyDomain object) {
		return vocabService.update(object);
	}

	public VocabularyDomain delete(Integer key) {
		return vocabService.delete(key);
	}

	public SearchResults<VocabularyDomain> searchByFields(Map<String, Object> postParams) {
		return vocabService.search(postParams);
	}

}

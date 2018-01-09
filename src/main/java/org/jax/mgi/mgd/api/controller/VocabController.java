package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.rest.interfaces.VocabRESTInterface;
import org.jax.mgi.mgd.api.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class VocabController extends BaseController implements VocabRESTInterface {

	@Inject
	private VocabService vocabService;
	
	private Logger log = Logger.getLogger(getClass());
	
	@Override
	public Vocabulary create(String api_access_token, Vocabulary object) {
		return vocabService.create(object);
	}

	@Override
	public Vocabulary get(Integer key) {
		return vocabService.get(key);
	}

	@Override
	public Vocabulary update(String api_access_token, Vocabulary object) {
		return vocabService.update(object);
	}

	@Override
	public Vocabulary delete(String api_access_token, Integer key) {
		return vocabService.delete(key);
	}

	@Override
	public SearchResults<Vocabulary> search(Map<String, Object> postParams) {
		return vocabService.search(postParams);
	}

}

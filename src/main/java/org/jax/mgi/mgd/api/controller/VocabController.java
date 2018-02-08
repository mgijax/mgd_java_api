package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.rest.interfaces.VocabRESTInterface;
import org.jax.mgi.mgd.api.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class VocabController extends BaseController implements VocabRESTInterface {

	@Inject
	private VocabService vocabService;

	@Override
	public VocabularyDomain create(String api_access_token, VocabularyDomain object) {
		if(authenticate(api_access_token)) {
			return vocabService.create(object);
		}
		return null;
	}

	@Override
	public VocabularyDomain get(Integer key) {
		return vocabService.get(key);
	}

	@Override
	public VocabularyDomain update(String api_access_token, VocabularyDomain object) {
		if(authenticate(api_access_token)) {
			return vocabService.update(object);
		}
		return null;
	}

	@Override
	public VocabularyDomain delete(String api_access_token, Integer key) {
		if(authenticate(api_access_token)) {
			return vocabService.delete(key);
		}
		return null;
	}

	@Override
	public SearchResults<VocabularyDomain> search(Map<String, Object> postParams) {
		return vocabService.search(postParams);
	}

}

package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class VocabController extends BaseController<VocabularyDomain> {

	@Inject
	private VocabService vocabService;

	@Override
	public VocabularyDomain create(VocabularyDomain object) {
		return vocabService.create(object);
	}

	@Override
	public VocabularyDomain get(Integer key) {
		return vocabService.get(key);
	}

	@Override
	public VocabularyDomain update(VocabularyDomain object) {
		return vocabService.update(object);
	}

	@Override
	public VocabularyDomain delete(Integer key) {
		return vocabService.delete(key);
	}

	@Override
	public SearchResults<VocabularyDomain> search(Map<String, Object> postParams) {
		return vocabService.search(postParams);
	}

}

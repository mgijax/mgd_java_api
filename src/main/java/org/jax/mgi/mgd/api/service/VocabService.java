package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.translators.VocabularyTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class VocabService extends ServiceInterface<VocabularyDomain> {

	@Inject
	private VocabularyDAO vocabularyDAO;
	private VocabularyTranslator translator = new VocabularyTranslator();
	
	@Transactional
	public VocabularyDomain create(VocabularyDomain object) {
		Vocabulary vocab = translator.translate(object);
		Vocabulary returnVocab = vocabularyDAO.create(vocab);
		VocabularyDomain vocabDomain = translator.translate(returnVocab);
		return vocabDomain;
	}

	@Transactional
	public VocabularyDomain update(VocabularyDomain object) {
		Vocabulary vocab = translator.translate(object);
		Vocabulary returnVocab = vocabularyDAO.update(vocab);
		VocabularyDomain vocabDomain = translator.translate(returnVocab);
		return vocabDomain;
	}

	public VocabularyDomain get(Integer key) {
		return translator.translate(vocabularyDAO.get(key));
	}
	
	public VocabularyDomain delete(Integer key) {
		return translator.translate(vocabularyDAO.delete(vocabularyDAO.get(key)));
	}

	@Transactional
	public SearchResults<VocabularyDomain> search(Map<String, Object> searchFields) {
		SearchResults<Vocabulary> vocabs = vocabularyDAO.search(searchFields);
		Iterable<VocabularyDomain> newItems = translator.translateEntities(vocabs.items);
		return new SearchResults<VocabularyDomain>(newItems);
	}
	
	@Transactional
	public SearchResults<VocabularyDomain> search(Map<String, Object> searchFields, String orderByField) {
		SearchResults<Vocabulary> vocabs = vocabularyDAO.search(searchFields, orderByField);
		Iterable<VocabularyDomain> newItems = translator.translateEntities(vocabs.items);
		return new SearchResults<VocabularyDomain>(newItems);
	}
	
}

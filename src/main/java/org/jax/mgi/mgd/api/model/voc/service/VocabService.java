package org.jax.mgi.mgd.api.model.voc.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.model.voc.search.VocabularySearchForm;
import org.jax.mgi.mgd.api.model.voc.translator.VocabularyTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class VocabService extends BaseService<VocabularyDomain> implements BaseSearchInterface<VocabularyDomain, VocabularySearchForm> {

	@Inject
	private VocabularyDAO vocabularyDAO;
	private VocabularyTranslator translator = new VocabularyTranslator();
	
	@Transactional
	public VocabularyDomain create(VocabularyDomain object, User user) throws APIException {
		Vocabulary vocab = translator.translate(object);
		Vocabulary returnVocab = vocabularyDAO.create(vocab);
		VocabularyDomain vocabDomain = translator.translate(returnVocab);
		return vocabDomain;
	}

	@Transactional
	public VocabularyDomain update(VocabularyDomain object, User user) {
		Vocabulary vocab = translator.translate(object);
		Vocabulary returnVocab = vocabularyDAO.update(vocab);
		VocabularyDomain vocabDomain = translator.translate(returnVocab);
		return vocabDomain;
	}

	@Transactional
	public VocabularyDomain get(Integer key) {
		return translator.translate(vocabularyDAO.get(key));
	}
	
	@Transactional
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
                // TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VocabularyDomain> search(VocabularySearchForm form) {
		SearchResults<Vocabulary> vocabs;
		if(form.getOrderBy() != null) {
			vocabs = vocabularyDAO.search(form.getSearchFields(), form.getOrderBy());
		} else {
			vocabs = vocabularyDAO.search(form.getSearchFields());
		}

		Iterable<VocabularyDomain> newItems = translator.translateEntities(vocabs.items, form.getSearchDepth());

		return new SearchResults<VocabularyDomain>(newItems);
	}
	
}

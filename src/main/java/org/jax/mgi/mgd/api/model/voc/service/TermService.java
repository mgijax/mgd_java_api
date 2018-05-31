package org.jax.mgi.mgd.api.model.voc.service;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.DuplicateEntryException;
import org.jax.mgi.mgd.api.exception.NotFoundException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.model.voc.search.TermSearchForm;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class TermService extends BaseService<TermDomain> implements BaseSearchInterface<TermDomain, TermSearchForm> {

	@Inject
	private TermDAO termDAO;
	
	@Inject
	private VocabularyDAO vocabDAO;
	
	@Inject
	private UserService userService;
	
	private TermTranslator translator = new TermTranslator();
	private Logger log = Logger.getLogger(getClass());
	
	@Transactional
	public TermDomain create(TermDomain termDomain, User user) throws APIException {
		/*
		The VOC_Term primary key is not auto-sequenced - we will need a way to get the next primary key available.

		Increment the max sequence number of the vocabulary OR user can specify sequenceNum. This requires 'shuffling' of sequence numbers above this (update).

		Optional *single* synonym for 'GO properties' vocabulary only. Single synonym is convention in the Teleuse/EI Simple Vocab Module. MGI_SynonymType._SynonymType_key = 1034 (MGI_GORel), MGI_SynonymType._MGIType_key = 13 (VOC_Term), VOC_Vocab._Vocab_key = 82 (GO Property)
		*/
		
		Vocabulary vocab = vocabDAO.getByName(termDomain.getVocabName());

		if(vocab == null) {
			throw new NotFoundException("Vocabulary not found for: " + termDomain.getVocabName());
		}
		
		for(Term t: vocab.getTerms()) {
			if(termDomain.getTerm().equals(t.getTerm())) {
				throw new DuplicateEntryException("Duplicate Term found for: " + termDomain.getTerm() + " in " + vocab.getName());
			}
		}
		
		Term term = translator.translate(termDomain);
		
		log.debug("Creating Term: " + term);
		
		Date now = new Date();
		term.setCreation_date(now);
		term.setCreatedBy(user);
		term.setModification_date(now);
		term.setModifiedBy(user);
		term.set_term_key(termDAO.getNextKey());
		term.setVocab(vocab);

		Term returnTerm = termDAO.create(term);
		log.info("Create finished: " + returnTerm);
		
		TermDomain returnTermDomain = translator.translate(returnTerm);
		
		log.info("Return Domain: " + returnTermDomain);
		
		return returnTermDomain;
	}

	@Transactional
	public TermDomain update(TermDomain object, User user) {
		Term term = translator.translate(object);
		
		term.setCreatedBy(userService.getUserByUsername(object.getCreatedBy()));
		term.setModifiedBy(user);
		
		Term returnTerm = termDAO.update(term);

		
		TermDomain termDomain = translator.translate(returnTerm);
		return termDomain;
	}
	
	@Transactional
	public TermDomain get(Integer key) {
		return translator.translate(termDAO.get(key));
	}
	
	@Transactional
	public TermDomain delete(Integer key, User user) {
		return translator.translate(termDAO.delete(termDAO.get(key)));
	}

	@Override
	public SearchResults<TermDomain> search(TermSearchForm searchForm) {
		SearchResults<Term> terms;
		if(searchForm.getOrderBy() != null) {
			terms = termDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			terms = termDAO.search(searchForm.getSearchFields());
		}
		Iterable<TermDomain> newItems = translator.translateEntities(terms.items, searchForm.getSearchDepth());
		return new SearchResults<TermDomain>(newItems);
	}
	
	
	

}

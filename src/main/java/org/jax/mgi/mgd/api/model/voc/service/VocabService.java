package org.jax.mgi.mgd.api.model.voc.service;

import java.util.HashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.DuplicateEntryException;
import org.jax.mgi.mgd.api.exception.NotFoundException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.model.voc.repository.VocabularyRepository;
import org.jax.mgi.mgd.api.model.voc.search.VocabularySearchForm;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class VocabService extends BaseService<VocabularyDomain> implements BaseSearchInterface<VocabularyDomain, VocabularySearchForm> {

	@Inject
	private VocabularyDAO vocabDAO;
	
	@Inject
	private VocabularyRepository vocabRepo;

	public VocabularyDomain create(VocabularyDomain object, User user) throws APIException {
//		The VOC_Term primary key is not auto-sequenced - we will need a way to get the next primary key available.
		//Vocabulary vocab = translator.translate(object);
		//Vocabulary returnVocab = vocabularyDAO.create(vocab);
		//VocabularyDomain vocabDomain = translator.translate(returnVocab);
		//return vocabDomain;
		return null;
	}

	public VocabularyDomain update(VocabularyDomain object, User user) throws APIException {
		
		// Checking that vocab exists
		Vocabulary vocab = vocabDAO.get(object.get_vocab_key());
		if(vocab == null) {
			throw new NotFoundException("Vocabulary not found for: Name: " + object.getName() + " Key: " + object.get_vocab_key());
		}
		
		// TODO Need to Implement flag for isCuratable in voc_vocab
//		if(vocab.getIsCurateable() == 0) {
//			throw new NotAllowedException("Vocabulary is not Curateable: " + vocab.getName());
//		}
		
		// Check for dups in domain object
		HashMap<String, String> dupmap = new HashMap<>();
		for(TermDomain td: object.getTerms()) {
			if(dupmap.get(td.getTerm()) == null) {
				dupmap.put(td.getTerm(), td.getTerm());
			} else {
				throw new DuplicateEntryException("Duplicate Term found for: " + td.getTerm() + " in " + object.getName());
			}
			
//			Optional *single* synonym for 'GO properties' vocabulary only. Single synonym is convention in the Teleuse/EI Simple Vocab Module. MGI_SynonymType._SynonymType_key = 1034 (MGI_GORel), MGI_SynonymType._MGIType_key = 13 (VOC_Term), VOC_Vocab._Vocab_key = 82 (GO Property)
			// TODO Need to Implement flag for hasSynonyms in voc_vocab
			// The UI needs to implement the rule for the amount of synonym per vocab type
//			if(vocab.getHasSynonyms() == 0 && td.getMGISynonyms().size() > 0) {
//				throw new NotAllowedException("Vocabulary: " + vocab.getName() + " does not allow Synonyms");
//			}
			
			// Increment the max sequence number of the vocabulary OR user can specify sequenceNum. This requires 'shuffling' of sequence numbers above this (update).
			// This requirement is implemented in the UI
		}

		return vocabRepo.update(object, user);

	}

	public VocabularyDomain get(Integer key) throws APIException {
		return vocabRepo.get(key);
	}
	
	public VocabularyDomain delete(Integer key, User user) throws APIException {
		return vocabRepo.delete(key, user);
	}

	public SearchResults<VocabularyDomain> search(VocabularySearchForm form) {
		if(form.getOrderBy() != null) {
			return vocabRepo.search(form.getSearchFields(), form.getOrderBy());
		} else {
			return vocabRepo.search(form.getSearchFields());
		}
	}

}

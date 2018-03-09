package org.jax.mgi.mgd.api.model.voc.repository;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseRepository;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.model.voc.translator.VocabularyTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class VocabularyRepository extends BaseRepository<VocabularyDomain> {

	@Inject
	private VocabularyDAO vocabDAO;
	
	private VocabularyTranslator translator = new VocabularyTranslator();
	
	


	@Override
	public VocabularyDomain create(VocabularyDomain domain, User username) throws APIException {
		
		
		return null;
	}

	@Override
	public VocabularyDomain update(VocabularyDomain domain, User user) throws APIException {

		//Vocabulary updateVocab = vocabDAO.get(domain.get_vocab_key());
		
		
		
		
		//Vocabulary vocab = translator.translate(object);
		
		//VocabularyDomain vocabDomain = translator.translate(returnVocab);
		//return vocabDomain;
			
			
			
			//private VocabularyTranslator translator = new VocabularyTranslator();

//			Date now = new Date();
//			if(td.get_term_key() == 0) {
//				// New Term
//				td.setIsObsolete(0);
//				td.setCreatedBy(user.getLogin());
//				td.setCreation_date(now);
//			}
		//	
//			td.setModifiedBy(user.getLogin());
//			td.setModification_date(now);
			
//			entity.setCreatedBy(domain.getCreatedBy());
//			entity.setModifiedBy(domain.getModifiedBy().getLogin());
//			entity.setVocabName(domain.getVocab().getName());
			

			
		
		return null;

	}

	@Override
	public VocabularyDomain delete(Integer key, User user) throws APIException {
		// TODO Auto-generated method stub
		return translator.translate(vocabDAO.delete(vocabDAO.get(key)));
	}

	@Override
	public VocabularyDomain get(Integer primaryKey) throws APIException {
		return translator.translate(vocabDAO.get(primaryKey));
	}

	@Override
	public SearchResults<VocabularyDomain> search(Map<String, Object> params) {
		SearchResults<Vocabulary> vocabs = vocabDAO.search(params);
		Iterable<VocabularyDomain> newItems = translator.translateEntities(vocabs.items);
		return new SearchResults<VocabularyDomain>(newItems);
	}

	@Override
	public SearchResults<VocabularyDomain> search(Map<String, Object> params, String orderBy) {
		SearchResults<Vocabulary> vocabs = vocabDAO.search(params, orderBy);
		Iterable<VocabularyDomain> newItems = translator.translateEntities(vocabs.items);
		return new SearchResults<VocabularyDomain>(newItems);
	}


	
	
	
	
}

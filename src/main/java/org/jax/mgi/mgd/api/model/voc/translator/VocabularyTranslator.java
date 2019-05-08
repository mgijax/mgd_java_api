package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class VocabularyTranslator extends BaseEntityDomainTranslator<Vocabulary, VocabularyDomain> {

	private TermTranslator termTranslator = new TermTranslator();
	
	@Override
	protected VocabularyDomain entityToDomain(Vocabulary entity, int translationDepth) {
		VocabularyDomain domain = new VocabularyDomain();
		
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		// for backward compatibilty with gxd/ht
		//domain.set_vocab_key(entity.get_vocab_key());
		domain.setIsSimple(entity.getIsSimple());
		domain.setIsPrivate(entity.getIsPrivate());
		domain.setName(entity.getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		Iterable<TermDomain> terms = termTranslator.translateEntities(entity.getTerms());
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}

}

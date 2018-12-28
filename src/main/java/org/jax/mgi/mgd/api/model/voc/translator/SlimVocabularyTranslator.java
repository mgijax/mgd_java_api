package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class SlimVocabularyTranslator extends BaseEntityDomainTranslator<Vocabulary, SlimVocabularyDomain> {

	private SlimTermTranslator termTranslator = new SlimTermTranslator();
	
	@Override
	protected SlimVocabularyDomain entityToDomain(Vocabulary entity, int translationDepth) {
		SlimVocabularyDomain domain = new SlimVocabularyDomain();
		
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		domain.setName(entity.getName());
		// for backward compatibility with gxd/littriage 
		domain.setVocabName(entity.getName());
		Iterable<SlimTermDomain> terms = termTranslator.translateEntities(entity.getTerms(), translationDepth - 1);
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}

	@Override
	protected Vocabulary domainToEntity(SlimVocabularyDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

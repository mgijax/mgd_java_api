package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyTermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class SlimVocabularyTermTranslator extends BaseEntityDomainTranslator<Vocabulary, SlimVocabularyTermDomain> {

	private SlimTermTranslator termTranslator = new SlimTermTranslator();
	//private SlimReferenceTranslator refTranslator = new SlimReferenceTranslator();
	
	@Override
	protected SlimVocabularyTermDomain entityToDomain(Vocabulary entity) {
		SlimVocabularyTermDomain domain = new SlimVocabularyTermDomain();
		
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		domain.setName(entity.getName());
		Iterable<SlimTermDomain> terms = termTranslator.translateEntities(entity.getTerms());
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}

}

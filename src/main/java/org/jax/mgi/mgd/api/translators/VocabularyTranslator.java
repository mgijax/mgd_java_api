package org.jax.mgi.mgd.api.translators;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class VocabularyTranslator extends EntityDomainTranslator<Vocabulary, VocabularyDomain> {

	private TermTranslator termTranslator = new TermTranslator();
	
	@Override
	protected VocabularyDomain entityToDomain(Vocabulary entity) {
		VocabularyDomain domain = new VocabularyDomain();
		
		domain.set_vocab_key(entity.get_vocab_key());
		domain.setIsSimple(entity.getIsSimple());
		domain.setIsPrivate(entity.getIsPrivate());
		domain.setName(entity.getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		Iterable<TermDomain> terms = termTranslator.translateEntities(entity.getTerms());
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		return domain;
	}

	@Override
	protected Vocabulary domainToEntity(VocabularyDomain domain) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

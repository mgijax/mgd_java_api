package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceCitationCacheTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class VocabularyTranslator extends BaseEntityDomainTranslator<Vocabulary, VocabularyDomain> {

	private TermTranslator termTranslator = new TermTranslator();
	private ReferenceCitationCacheTranslator referenceTranslator = new ReferenceCitationCacheTranslator();

	@Override
	protected VocabularyDomain entityToDomain(Vocabulary entity) {
		VocabularyDomain domain = new VocabularyDomain();
		
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		domain.setIsSimple(entity.getIsSimple());
		domain.setIsPrivate(entity.getIsPrivate());
		domain.setName(entity.getName());
		domain.setReference(referenceTranslator.translate(entity.getReference()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
			
		Iterable<TermDomain> terms = termTranslator.translateEntities(entity.getTerms());
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}
}

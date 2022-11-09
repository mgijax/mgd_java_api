package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceCitationCacheTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jboss.logging.Logger;

public class VocabularyTranslator extends BaseEntityDomainTranslator<Vocabulary, VocabularyDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private TermTranslator termTranslator = new TermTranslator();
	private ReferenceCitationCacheTranslator referenceTranslator = new ReferenceCitationCacheTranslator();

	@Override
	protected VocabularyDomain entityToDomain(Vocabulary entity) {
		VocabularyDomain domain = new VocabularyDomain();
		
		log.info(entity.get_vocab_key());
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));

		log.info(entity.getIsSimple());
		domain.setIsSimple(entity.getIsSimple());

		log.info(entity.getIsPrivate());		
		domain.setIsPrivate(entity.getIsPrivate());

		log.info(entity.getName());
		domain.setName(entity.getName());

		log.info(entity.getReference());
		domain.setReference(referenceTranslator.translate(entity.getReference()));

		log.info(entity.getCreation_date());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));

		log.info(entity.getModification_date());		
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
			
		log.info(entity.getTerms());	
		Iterable<TermDomain> terms = termTranslator.translateEntities(entity.getTerms());
		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}
}

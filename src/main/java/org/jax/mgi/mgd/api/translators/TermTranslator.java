package org.jax.mgi.mgd.api.translators;

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class TermTranslator extends EntityDomainTranslator<Term, TermDomain> {

	@Override
	protected TermDomain entityToDomain(Term entity) {
		TermDomain domain = new TermDomain();

		domain.set_term_key(entity.get_term_key());
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setIsObsolete(entity.getIsObsolete());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setVocabName(entity.getVocab().getName());
		return domain;
	}

	@Override
	protected Term domainToEntity(TermDomain domain) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

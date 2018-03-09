package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class TermTranslator extends BaseEntityDomainTranslator<Term, TermDomain> {

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
	protected Term createEntityFromDomain(TermDomain domain) {
		Term entity = new Term();
		entity.set_term_key(domain.get_term_key());
		entity.setTerm(domain.getTerm());
		entity.setAbbreviation(domain.getAbbreviation());
		entity.setSequenceNum(domain.getSequenceNum());
		entity.setIsObsolete(domain.getIsObsolete());
		entity.setCreation_date(domain.getCreation_date());
		entity.setModification_date(domain.getModification_date());
		return entity;
	}

	@Override
	protected Term updateEntityFromDomain(TermDomain domain, Term entity) {
		// TODO Auto-generated method stub
		return null;
	}

}

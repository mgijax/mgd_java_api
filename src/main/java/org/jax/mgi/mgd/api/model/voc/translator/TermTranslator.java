package org.jax.mgi.mgd.api.model.voc.translator;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class TermTranslator extends BaseEntityDomainTranslator<Term, TermDomain> {
	
	/* (non-Javadoc)
	 * @see org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator#entityToDomain(org.jax.mgi.mgd.api.model.BaseEntity, int)
	 */
	
	@Inject
	private VocabularyDAO vocabDAO;
	
	@Override
	protected TermDomain entityToDomain(Term entity, int translationDepth) {
		TermDomain domain = new TermDomain();
		
		domain.set_term_key(entity.get_term_key());
		domain.setVocabKey(String.valueOf(entity.getVocab().get_vocab_key()));
		domain.setVocabName(entity.getVocab().getName());
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setNote(entity.getNote());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setIsObsolete(entity.getIsObsolete());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
	
		return domain;
	}

	@Override
	protected Term domainToEntity(TermDomain domain, int translationDepth) {
		Term entity = new Term();
		
		entity.set_term_key(domain.get_term_key());
		entity.setTerm(domain.getTerm());
		entity.setVocab(vocabDAO.get(Integer.valueOf(domain.getVocabKey())));
		entity.setAbbreviation(domain.getAbbreviation());
		entity.setNote(domain.getNote());
		entity.setSequenceNum(domain.getSequenceNum());
		entity.setIsObsolete(domain.getIsObsolete());
		//entity.setCreatedBy()
		//entity.setModifiedBy()
		entity.setCreation_date(domain.getCreation_date());
		entity.setModification_date(domain.getModification_date());
		
		return entity;
	}

}

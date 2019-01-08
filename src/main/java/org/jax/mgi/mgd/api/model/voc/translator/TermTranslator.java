package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class TermTranslator extends BaseEntityDomainTranslator<Term, TermDomain> {
	
	AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected TermDomain entityToDomain(Term entity, int translationDepth) {
		TermDomain domain = new TermDomain();
		
		domain.setTermKey(String.valueOf(entity.get_term_key()));
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		//domain.setVocabKey(String.valueOf(entity.getVocab().get_vocab_key()));
		//domain.setVocabName(entity.getVocab().getName());
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setNote(entity.getNote());
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setIsObsolete(String.valueOf(entity.getIsObsolete()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
	
		Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionIds());
		if(acc.iterator().hasNext() == true) {
			domain.setAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		return domain;
	}

	@Override
	protected Term domainToEntity(TermDomain domain, int translationDepth) {
		//Term entity = new Term();
		
		//entity.set_term_key(Integer.valueOf(domain.getTermKey()));
		//entity.setTerm(domain.getTerm());
		//entity.setVocab(vocabDAO.get(Integer.valueOf(domain.getVocabKey())));
		//entity.setAbbreviation(domain.getAbbreviation());
		//entity.setNote(domain.getNote());
		//entity.setSequenceNum(Integer.valueOf(domain.getSequenceNum()));
		//entity.setIsObsolete(Integer.valueOf(domain.getIsObsolete()));
		//entity.setCreatedBy()
		//entity.setModifiedBy()
		//entity.setCreation_date(domain.getCreation_date());
		//entity.setModification_date(domain.getModification_date());
		
		//return entity;
		
		// not used
		return null;
	}

}

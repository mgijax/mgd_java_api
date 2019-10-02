package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class SlimTermTranslator extends BaseEntityDomainTranslator<Term, SlimTermDomain> {
	
	@Override
	protected SlimTermDomain entityToDomain(Term entity) {
		SlimTermDomain domain = new SlimTermDomain();
		
		domain.setTermKey(String.valueOf(entity.get_term_key()));
		// for backward compatibility with GXD UI
		domain.set_term_key(entity.get_term_key());
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setVocabKey(entity.get_vocab_key().toString());

		return domain;
	}

}

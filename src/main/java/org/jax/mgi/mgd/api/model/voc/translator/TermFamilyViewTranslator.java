package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyViewDomain;
import org.jax.mgi.mgd.api.model.voc.entities.TermFamilyView;
import org.jboss.logging.Logger;

public class TermFamilyViewTranslator extends BaseEntityDomainTranslator<TermFamilyView, TermFamilyViewDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected TermFamilyViewDomain entityToDomain(TermFamilyView entity) {
		TermFamilyViewDomain domain = new TermFamilyViewDomain();
		
		domain.setTermKey(String.valueOf(entity.get_term_key()));
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		domain.setAccid(entity.getAccid());
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setNote(entity.getNote());
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setIsObsolete(String.valueOf(entity.getIsObsolete()));
		domain.setCreatedByKey(String.valueOf(entity.get_creatdby_key()));
		domain.setModifiedByKey(String.valueOf(entity.get_modifiedby_key()));		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
       
       return domain;
	}

}

package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.EvidenceProperty;
import org.jax.mgi.mgd.api.util.Constants;

public class EvidencePropertyTranslator extends BaseEntityDomainTranslator<EvidenceProperty, EvidencePropertyDomain> {
	
	@Override
	protected EvidencePropertyDomain entityToDomain(EvidenceProperty entity) {
		EvidencePropertyDomain domain = new EvidencePropertyDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setEvidencePropertyKey(String.valueOf(entity.get_evidenceProperty_key()));
		domain.setAnnotEvidenceKey(String.valueOf(entity.get_annotEvidence_key())); 
		domain.setPropertyTermKey(String.valueOf(entity.getPropertyTerm().get_term_key()));
		domain.setPropertyTerm(String.valueOf(entity.getPropertyTerm().getTerm()));
		domain.setStanza(entity.getStanza());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setValue(entity.getValue());	
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));		
			
		return domain;
	}

}

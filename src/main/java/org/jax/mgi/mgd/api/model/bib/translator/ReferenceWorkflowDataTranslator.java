package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowData;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ReferenceWorkflowDataTranslator extends BaseEntityDomainTranslator<ReferenceWorkflowData, ReferenceWorkflowDataDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected ReferenceWorkflowDataDomain entityToDomain(ReferenceWorkflowData entity) {

		ReferenceWorkflowDataDomain domain = new ReferenceWorkflowDataDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);	
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setHaspdf(String.valueOf(entity.getHaspdf()));
		domain.setSupplementalKey(String.valueOf(entity.getSupplementalTerm().get_term_key()));
		domain.setSupplementalTerm(entity.getSupplementalTerm().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

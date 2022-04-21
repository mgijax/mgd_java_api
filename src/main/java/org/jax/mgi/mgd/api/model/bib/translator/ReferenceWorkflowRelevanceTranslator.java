package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowRelevance;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ReferenceWorkflowRelevanceTranslator extends BaseEntityDomainTranslator<ReferenceWorkflowRelevance, ReferenceWorkflowRelevanceDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected ReferenceWorkflowRelevanceDomain entityToDomain(ReferenceWorkflowRelevance entity) {

		ReferenceWorkflowRelevanceDomain domain = new ReferenceWorkflowRelevanceDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);	
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setIsCurrent(String.valueOf(entity.getIsCurrent()));
		domain.setRelevanceKey(String.valueOf(entity.getRelevanceTerm().get_term_key()));
		domain.setRelevance(entity.getRelevanceTerm().getTerm());
		domain.setConfidence(String.valueOf(entity.getConfidence()));
		domain.setVersion(entity.getVersion());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

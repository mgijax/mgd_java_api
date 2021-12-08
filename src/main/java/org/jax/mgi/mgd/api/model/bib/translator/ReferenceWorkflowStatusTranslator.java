package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ReferenceWorkflowStatusTranslator extends BaseEntityDomainTranslator<ReferenceWorkflowStatus, ReferenceWorkflowStatusDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ReferenceWorkflowStatusDomain entityToDomain(ReferenceWorkflowStatus entity) {

		ReferenceWorkflowStatusDomain domain = new ReferenceWorkflowStatusDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setGroupKey(String.valueOf(entity.getGroupTerm().get_term_key()));
		domain.setGroupTerm(entity.getGroupTerm().getTerm());
		domain.setStatusKey(String.valueOf(entity.getStatusTerm().get_term_key()));
		domain.setStatusTerm(entity.getStatusTerm().getTerm());
		
		if (entity.getIsCurrent() == 1) {
			domain.setIsCurrent(true);
		}
		else {
			domain.setIsCurrent(false);
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

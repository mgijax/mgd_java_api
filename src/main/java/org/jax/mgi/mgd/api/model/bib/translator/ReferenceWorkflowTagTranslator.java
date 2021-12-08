package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowTagDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowTag;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ReferenceWorkflowTagTranslator extends BaseEntityDomainTranslator<ReferenceWorkflowTag, ReferenceWorkflowTagDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ReferenceWorkflowTagDomain entityToDomain(ReferenceWorkflowTag entity) {

		ReferenceWorkflowTagDomain domain = new ReferenceWorkflowTagDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));		
		domain.setTagKey(String.valueOf(entity.getTagTerm().get_term_key()));
		domain.setTagTerm(entity.getTagTerm().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

}

package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AnnotationHeaderTranslator extends BaseEntityDomainTranslator<AnnotationHeader, AnnotationHeaderDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AnnotationHeaderDomain entityToDomain(AnnotationHeader entity) {
		AnnotationHeaderDomain domain = new AnnotationHeaderDomain();
				
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotHeaderKey(String.valueOf(entity.get_annotheader_key()));
		domain.setAnnotTypeKey(String.valueOf(entity.getAnnotType().get_annotType_key()));
		domain.setAnnotType(entity.getAnnotType().getName());
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		domain.setApprovedByKey(entity.getApprovedBy().get_user_key().toString());
		domain.setApprovedBy(entity.getApprovedBy().getLogin());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setApproval_date(dateFormatNoTime.format(entity.getApproval_date()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
	
		return domain;
	}

}

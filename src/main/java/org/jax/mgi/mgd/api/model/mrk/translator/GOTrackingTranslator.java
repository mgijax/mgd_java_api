package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.GOTrackingDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.GOTracking;
import org.jax.mgi.mgd.api.util.Constants;

public class GOTrackingTranslator extends BaseEntityDomainTranslator<GOTracking, GOTrackingDomain> {

	@Override
	protected GOTrackingDomain entityToDomain(GOTracking entity) {
		GOTrackingDomain domain = new GOTrackingDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setCompletedByKey(entity.getCompletedBy().get_user_key().toString());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCompletion_date(dateFormatNoTime.format(entity.getCompletion_date()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

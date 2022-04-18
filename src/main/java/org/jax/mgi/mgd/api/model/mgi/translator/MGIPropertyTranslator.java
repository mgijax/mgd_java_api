package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIPropertyTranslator extends BaseEntityDomainTranslator<MGIProperty, MGIPropertyDomain> {

	@Override
	protected MGIPropertyDomain entityToDomain(MGIProperty entity) {
		MGIPropertyDomain domain = new MGIPropertyDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setPropertyKey(String.valueOf(entity.get_property_key()));
/*
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setSynonymTypeKey(String.valueOf(entity.getSynonymType().get_synonymType_key()));
	    domain.setSynonymType(entity.getSynonymType().getSynonymType());
		domain.setSynonym(entity.getSynonym());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
*/

		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIRefAssocTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIRefAssocType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIRefAssocTypeTranslator extends BaseEntityDomainTranslator<MGIRefAssocType, MGIRefAssocTypeDomain> {

	@Override
	protected MGIRefAssocTypeDomain entityToDomain(MGIRefAssocType entity, int translationDepth) {
		MGIRefAssocTypeDomain domain = new MGIRefAssocTypeDomain();
		
		domain.setRefAssocTypeKey(String.valueOf(entity.get_refAssocType_key()));
		domain.setAssocType(entity.getAssocType());
		domain.setAllowOnlyOne(String.valueOf(entity.getAllowOnlyOne()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getMgiType() != null) {
			domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		}
		
		return domain;
	}

	@Override
	protected MGIRefAssocType domainToEntity(MGIRefAssocTypeDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

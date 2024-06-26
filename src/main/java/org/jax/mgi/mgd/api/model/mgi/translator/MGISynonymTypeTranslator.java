package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonymType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISynonymTypeTranslator extends BaseEntityDomainTranslator<MGISynonymType, MGISynonymTypeDomain> {

	@Override
	protected MGISynonymTypeDomain entityToDomain(MGISynonymType entity) {
		MGISynonymTypeDomain domain = new MGISynonymTypeDomain();
		
		domain.setSynonymTypeKey(String.valueOf(entity.get_synonymType_key()));
		domain.setSynonymType(entity.getSynonymType());
		domain.setDefinition(entity.getDefinition());
		domain.setAllowOnlyOne(String.valueOf(entity.getAllowOnlyOne()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
				
		if (entity.getOrganism() != null) {
			domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

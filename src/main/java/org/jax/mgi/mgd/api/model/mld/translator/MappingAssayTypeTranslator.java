package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.MappingAssayTypeDomain;
import org.jax.mgi.mgd.api.model.mld.entities.MappingAssayType;

public class MappingAssayTypeTranslator extends BaseEntityDomainTranslator<MappingAssayType, MappingAssayTypeDomain> {

	@Override
	protected MappingAssayTypeDomain entityToDomain(MappingAssayType entity) {

		MappingAssayTypeDomain domain = new MappingAssayTypeDomain();

		domain.setAssayTypeKey(String.valueOf(entity.get_assay_type_key()));
		domain.setDescription(entity.getDescription());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

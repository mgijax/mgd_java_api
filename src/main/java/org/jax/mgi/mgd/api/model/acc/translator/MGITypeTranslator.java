package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

public class MGITypeTranslator extends BaseEntityDomainTranslator<MGIType, MGITypeDomain> {
	
	@Override
	protected MGITypeDomain entityToDomain(MGIType entity) {
		MGITypeDomain domain = new MGITypeDomain();

		domain.setMgiTypeKey(String.valueOf(entity.get_mgitype_key()));
		domain.setName(entity.getName());
		domain.setTableName(entity.getTableName());
		domain.setPrimaryKeyName(entity.getPrimaryKeyName());
		domain.setIdentityColumnName(entity.getIdentityColumnName());
		domain.setDbView(entity.getDbView());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());

		return domain;
	}

}

package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDbDomain;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;

public class ActualDbTranslator extends BaseEntityDomainTranslator<ActualDB, ActualDbDomain> {

	@Override
	protected ActualDbDomain entityToDomain(ActualDB entity) {
		ActualDbDomain domain = new ActualDbDomain();

		domain.setActualDBKey(String.valueOf(entity.get_actualdb_key()));
		domain.setActive(String.valueOf(entity.get_actualdb_key()));
		domain.setLogicalDBKey(String.valueOf(entity.get_logicaldb_key()));
		domain.setName(entity.getName());
		domain.setActive(String.valueOf(entity.getActive()));
		domain.setUrl(entity.getUrl());
		domain.setAllowsMultiple(String.valueOf(entity.getAllowsMultiple()));
		if (entity.getDelimiter() != null && !entity.getDelimiter().isEmpty()) {
			domain.setDelimiter(entity.getDelimiter());
		}
		
		domain.setCreatedBy(entity.getCreatedBy().getLogin().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin().toString());
		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}
}

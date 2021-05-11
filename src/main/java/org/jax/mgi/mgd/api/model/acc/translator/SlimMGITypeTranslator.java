package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

public class SlimMGITypeTranslator extends BaseEntityDomainTranslator<MGIType, SlimMGITypeDomain> {
	
	@Override
	protected SlimMGITypeDomain entityToDomain(MGIType entity) {
		SlimMGITypeDomain domain = new SlimMGITypeDomain();

		domain.setMgiTypeKey(String.valueOf(entity.get_mgitype_key()));
		domain.setName(entity.getName());

		return domain;
	}

}

package org.jax.mgi.mgd.api.model.acc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.SlimOrganismTranslator;

public class SlimMGITypeTranslator extends BaseEntityDomainTranslator<MGIType, SlimMGITypeDomain> {

	private SlimOrganismTranslator organismTranslator = new SlimOrganismTranslator();
	
	@Override
	protected SlimMGITypeDomain entityToDomain(MGIType entity) {
		SlimMGITypeDomain domain = new SlimMGITypeDomain();

		domain.setMgiTypeKey(String.valueOf(entity.get_mgitype_key()));
		domain.setName(entity.getName());

		Iterable<SlimOrganismDomain> terms = organismTranslator.translateEntities(entity.getOrganisms());
		domain.setOrganisms(IteratorUtils.toList(terms.iterator()));

		return domain;
	}

}

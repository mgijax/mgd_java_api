package org.jax.mgi.mgd.api.translators;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

public class MGITypeTranslator extends EntityDomainTranslator<MGIType, MGITypeDomain> {

	private OrganismTranslator organismTranslator = new OrganismTranslator();
	
	@Override
	protected MGITypeDomain entityToDomain(MGIType entity) {
		MGITypeDomain domain = new MGITypeDomain();

		domain.set_mgitype_key(entity.get_mgitype_key());
		domain.setName(entity.getName());
		domain.setTableName(entity.getTableName());
		domain.setPrimaryKeyName(entity.getPrimaryKeyName());
		domain.setIdentityColumnName(entity.getIdentityColumnName());
		domain.setDbView(entity.getDbView());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());

		Iterable<OrganismDomain> terms = organismTranslator.translateEntities(entity.getOrganisms());
		domain.setOrganisms(IteratorUtils.toList(terms.iterator()));

		return domain;
	}

	@Override
	protected MGIType domainToEntity(MGITypeDomain domain) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

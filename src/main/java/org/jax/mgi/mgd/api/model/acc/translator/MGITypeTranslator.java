package org.jax.mgi.mgd.api.model.acc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;

public class MGITypeTranslator extends BaseEntityDomainTranslator<MGIType, MGITypeDomain> {

	private OrganismTranslator organismTranslator = new OrganismTranslator();
	
	@Override
	protected MGITypeDomain entityToDomain(MGIType entity, int translationDepth) {
		MGITypeDomain domain = new MGITypeDomain();

		domain.setMgiTypeKey(String.valueOf(entity.get_mgitype_key()));
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

}

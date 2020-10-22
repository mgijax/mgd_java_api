package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

public class OrganismTranslator extends BaseEntityDomainTranslator<Organism, OrganismDomain> {

	@Override
	protected OrganismDomain entityToDomain(Organism entity) {
		OrganismDomain domain = new OrganismDomain();

		domain.set_organism_key(String.valueOf(entity.get_organism_key()));
		domain.setCommonname(entity.getCommonname());
		domain.setLatinname(entity.getLatinname());
		domain.setFullName(domain.getCommonname() + " (" + domain.getLatinname() + ")");
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

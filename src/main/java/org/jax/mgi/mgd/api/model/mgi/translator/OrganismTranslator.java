package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

public class OrganismTranslator extends BaseEntityDomainTranslator<Organism, OrganismDomain> {

	@Override
	protected OrganismDomain entityToDomain(Organism entity, int translationDepth) {
		OrganismDomain domain = new OrganismDomain();
		domain.set_organism_key(entity.get_organism_key());
		domain.setCommonname(entity.getCommonname());
		domain.setLatinname(entity.getLatinname());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected Organism domainToEntity(OrganismDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

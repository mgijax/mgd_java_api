package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

public class SlimOrganismTranslator extends BaseEntityDomainTranslator<Organism, SlimOrganismDomain> {

	@Override
	protected SlimOrganismDomain entityToDomain(Organism entity, int translationDepth) {
		SlimOrganismDomain domain = new SlimOrganismDomain();
		
		domain.set_organism_key(entity.get_organism_key());
		domain.setCommonname(entity.getCommonname());

		return domain;
	}

	@Override
	protected Organism domainToEntity(SlimOrganismDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

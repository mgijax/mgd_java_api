package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;

public class ChromosomeTranslator extends BaseEntityDomainTranslator<Chromosome, ChromosomeDomain> {

	@Override
	protected ChromosomeDomain entityToDomain(Chromosome entity) {
		ChromosomeDomain domain = new ChromosomeDomain();
		domain.setChromosomeKey(String.valueOf(entity.get_chromosome_key()));
		domain.setChromosome(entity.getChromosome());
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setCommonname(entity.getOrganism().getCommonname());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

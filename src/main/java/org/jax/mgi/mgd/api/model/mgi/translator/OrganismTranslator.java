package org.jax.mgi.mgd.api.model.mgi.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismMGITypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.ChromosomeTranslator;

public class OrganismTranslator extends BaseEntityDomainTranslator<Organism, OrganismDomain> {

	@Override
	protected OrganismDomain entityToDomain(Organism entity) {
		OrganismDomain domain = new OrganismDomain();

		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setCommonname(entity.getCommonname());
		domain.setLatinname(entity.getLatinname());
		domain.setFullName(domain.getCommonname() + " (" + domain.getLatinname() + ")");
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// mgi types
		if (entity.getMgiTypes() != null && !entity.getMgiTypes().isEmpty()) {
			OrganismMGITypeTranslator mgitypeTranslator = new OrganismMGITypeTranslator();
			Iterable<OrganismMGITypeDomain> i = mgitypeTranslator.translateEntities(entity.getMgiTypes());
			domain.setMgiTypes(IteratorUtils.toList(i.iterator()));
		}
	
		// chromosomes
		if (entity.getChromosomes() != null && !entity.getChromosomes().isEmpty()) {
			ChromosomeTranslator chrTranslator = new ChromosomeTranslator();
			Iterable<ChromosomeDomain> i = chrTranslator.translateEntities(entity.getChromosomes());
			domain.setChromosomes(IteratorUtils.toList(i.iterator()));
		}
			
		return domain;
	}

}

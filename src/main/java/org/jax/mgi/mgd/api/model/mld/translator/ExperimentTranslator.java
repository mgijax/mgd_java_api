package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Experiment;

public class ExperimentTranslator extends BaseEntityDomainTranslator<Experiment, ExperimentDomain> {

	@Override
	protected ExperimentDomain entityToDomain(Experiment entity, int translationDepth) {
		
		ExperimentDomain domain = new ExperimentDomain();
		domain.set_expt_key(entity.get_expt_key());
		domain.setExptType(entity.getExptType());
		domain.setTag(entity.getTag());
		domain.setChromosome(entity.getChromosome());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Experiment domainToEntity(ExperimentDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

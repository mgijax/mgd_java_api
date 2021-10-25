package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;

import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;

import org.jboss.logging.Logger;

public class HTExperimentVariableTranslator extends BaseEntityDomainTranslator<HTExperimentVariable, HTExperimentVariableDomain> {
	
	@Override
	protected HTExperimentVariableDomain entityToDomain(HTExperimentVariable entity) {

		Logger log = Logger.getLogger(getClass());

		HTExperimentVariableDomain domain = new HTExperimentVariableDomain();

		// Experiment Variable Info
		domain.set_experimentvariable_key(entity.get_experimentvariable_key());
		domain.setTerm(entity.getTerm().getTerm());
		domain.setAbbreviation(entity.getTerm().getTerm());
		domain.set_term_key(entity.getTerm().get_term_key());
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setChecked(true);
		domain.setVocabKey("122");

		return domain;
	}

}

















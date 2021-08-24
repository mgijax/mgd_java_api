package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

public class HTExperimentTranslator extends BaseEntityDomainTranslator<HTExperiment, HTDomain> {
	
	@Override
	protected HTDomain entityToDomain(HTExperiment entity) {
			
		HTDomain domain = new HTDomain();
		
		// basic experiment info
		domain.set_experiment_key(entity.get_experiment_key());
		domain.setName(entity.getName());
		domain.setDescription(entity.getDescription());
		if (entity.getPrimaryIDs() != null && !entity.getPrimaryIDs().isEmpty()) {
			domain.setPrimaryid(entity.getPrimaryIDs().get(0).getAccID());
		}
		if (entity.getSecondaryIDs() != null && !entity.getSecondaryIDs().isEmpty()) {
			domain.setSecondaryid(entity.getSecondaryIDs().get(0).getAccID());
		}
		//types and states
		domain.set_evaluationstate_key(entity.get_evaluationstate_key());
		domain.set_studytype_key(entity.get_studytype_key());
		domain.set_experimenttype_key(entity.get_experimenttype_key());
		domain.set_curationstate_key(entity.get_curationstate_key());

		// dates
		if (entity.getRelease_date() != null) {
			domain.setRelease_date(dateFormatNoTime.format(entity.getRelease_date()));
		}
		if (entity.getLastupdate_date() != null) {
			domain.setLastupdate_date(dateFormatNoTime.format(entity.getLastupdate_date()));
		}
		if (entity.getEvaluated_date() != null) {
			domain.setEvaluated_date(dateFormatNoTime.format(entity.getEvaluated_date()));
		}
		if (entity.getInitial_curated_date() != null) {
			domain.setInitial_curated_date(dateFormatNoTime.format(entity.getInitial_curated_date()));
		}
		if (entity.getLast_curated_date() != null) {
			domain.setLast_curated_date(dateFormatNoTime.format(entity.getLast_curated_date()));
		}
		if (entity.getCreation_date() != null) {
			domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		}

		// curator info
		if (entity.getEvaluatedBy() != null) {
			HTUserDomain userDomain = new HTUserDomain();
			userDomain.setLogin(entity.getEvaluatedBy().getLogin());
			domain.setEvaluatedby_object(userDomain);
		}

		if (entity.getInitialcuratedBy() != null) {
			HTUserDomain userDomain = new HTUserDomain();
			userDomain.setLogin(entity.getInitialcuratedBy().getLogin());
			domain.setInitialcuratedby_object(userDomain);
		}

		if (entity.getLastcuratedBy() != null) {
			HTUserDomain userDomain = new HTUserDomain();
			userDomain.setLogin(entity.getLastcuratedBy().getLogin());
			domain.setLastcuratedby_object(userDomain);
		}

		return domain;
	}

}

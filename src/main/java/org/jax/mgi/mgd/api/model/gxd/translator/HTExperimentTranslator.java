package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSourceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

import org.jboss.logging.Logger;

public class HTExperimentTranslator extends BaseEntityDomainTranslator<HTExperiment, HTDomain> {
	
	@Override
	protected HTDomain entityToDomain(HTExperiment entity) {

		Logger log = Logger.getLogger(getClass());
			
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
		if (entity.getSourceTerm() != null) {
			Term sourceTerm = entity.getSourceTerm();
			HTSourceDomain sourceDomain = new HTSourceDomain();
			sourceDomain.setAbbreviation(sourceTerm.getAbbreviation());
			sourceDomain.setTerm(sourceTerm.getTerm());

			domain.setSource_object(sourceDomain);
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


		// multiple 1-N values are held in properties
		if (entity.getProperties() != null) {

			List<String> pubmed_ids = new ArrayList<String>();
			List<String> experimental_factors = new ArrayList<String>(); 
			List<String> experiment_types = new ArrayList<String>();
			List<String> provider_contact_names = new ArrayList<String>();

			List<MGIProperty> properties = entity.getProperties();
			for (MGIProperty prop : properties) {
				//log.info(prop.getValue());

				if (prop.getPropertyTerm().get_term_key() == 20475430) {
					pubmed_ids.add(prop.getValue());
				}
				if (prop.getPropertyTerm().get_term_key() == 20475423) {
					experimental_factors.add(prop.getValue());
				}
				if (prop.getPropertyTerm().get_term_key() == 20475425) {
					experiment_types.add(prop.getValue());
				}
				if (prop.getPropertyTerm().get_term_key() == 20475426) {
					provider_contact_names.add(prop.getValue());
				}
			}

			// send them if we got them...
			if (pubmed_ids.size() > 0) {
				domain.setPubmed_ids(pubmed_ids);
			}
			if (experimental_factors.size() > 0) {
				domain.setExperimental_factors(experimental_factors);
			}
			if (experiment_types.size() > 0) {
				domain.setExperiment_types(experiment_types);
			}
			if (provider_contact_names.size() > 0) {
				domain.setProvider_contact_names(provider_contact_names);
			}

		}

		// experiment variables
		if (entity.getExperiment_variables() != null) {
			List<HTVariableDomain> experiment_variables = new ArrayList<HTVariableDomain>();
			for (HTExperimentVariable expVar : entity.getExperiment_variables()) {
				HTVariableDomain varDom = new HTVariableDomain(); 
				varDom.setTerm(expVar.getTerm().getTerm());
				varDom.setAbbreviation(expVar.getTerm().getTerm());
				varDom.setVocabKey("122");
				varDom.set_term_key(expVar.getTerm().get_term_key());
				varDom.setChecked(true);
				experiment_variables.add(varDom);
			}
			domain.setExperiment_variables(experiment_variables);
		}

		// notes
		if (entity.getNotes() != null) {
			String notetext = entity.getNotes().get(0).getNoteChunk().getNote();
			domain.setNotetext(notetext);
		}

		return domain;
	}

}

















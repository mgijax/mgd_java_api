package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.List;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain; 
import org.jax.mgi.mgd.api.model.gxd.domain.HTSourceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
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

		// setting display default 
		domain.setDeletingPubmedIds(0);  
		domain.setDeletingSamples(0);  
		domain.setCreatingSamples(0);  
		domain.setModifyingSamples(0); 

		domain.setHasSamples(0); // may be over-ridden below

		if (entity.getConfidence() != null) {
			domain.setConfidence(entity.getConfidence());
		}

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
		if (entity.getEvaluationState() != null) {
			domain.set_evaluationstate_key(entity.getEvaluationState().get_term_key());
		}
		if (entity.getStudyType() != null) {
			domain.set_studytype_key(entity.getStudyType().get_term_key());
		}
		if (entity.getExperimentType() != null) {
			domain.set_experimenttype_key(entity.getExperimentType().get_term_key());
		}
		if (entity.getCurationState() != null) {
			domain.set_curationstate_key(entity.getCurationState().get_term_key());
		}

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
			List<String> pubmed_property_keys = new ArrayList<String>();
			List<String> experimental_factors = new ArrayList<String>(); 
			List<String> experiment_types = new ArrayList<String>();
			List<String> provider_contact_names = new ArrayList<String>();

			List<MGIProperty> properties = entity.getProperties();
			for (MGIProperty prop : properties) {

				if (prop.getPropertyTerm().get_term_key() == 20475430) {
					pubmed_ids.add(prop.getValue());
					pubmed_property_keys.add(prop.get_property_key().toString());
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
				domain.setPubmed_property_keys(pubmed_property_keys);
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

		// notes
		if (entity.getNotes() != null && entity.getNotes().size() > 0) {
			List<Note> notes = entity.getNotes();
			//log.info(notes.size());
			String notetext = entity.getNotes().get(0).getNoteChunk().getNote();
			domain.set_note_key(String.valueOf(entity.getNotes().get(0).get_note_key()));
			domain.setNotetext(notetext);
		}

		// experiment variables
		if (entity.getExperiment_variables() != null) {
			List<HTExperimentVariableDomain> experiment_variables = new ArrayList<HTExperimentVariableDomain>();
			for (HTExperimentVariable experimentVariable : entity.getExperiment_variables()) {
				HTExperimentVariableDomain variableDomain = new HTExperimentVariableDomain();
				HTExperimentVariableTranslator variableTranslator = new HTExperimentVariableTranslator();
				variableDomain = variableTranslator.translate(experimentVariable);
				experiment_variables.add(variableDomain);
			}
			domain.setExperiment_variables(experiment_variables);
		}

		// experiment samples
		if (entity.getSamples() != null) {
			domain.setHasSamples(1); // setting display default 
			List<HTSampleDomain> samples = new ArrayList<HTSampleDomain>();
			for (HTSample sample : entity.getSamples()) {
				HTSampleDomain sampleDomain = new HTSampleDomain();
				HTSampleTranslator htSampleTranslator = new HTSampleTranslator();
				sampleDomain = htSampleTranslator.translate(sample);
				samples.add(sampleDomain);
			}
			domain.setSamples(samples);
		}

		return domain;
	}

}

















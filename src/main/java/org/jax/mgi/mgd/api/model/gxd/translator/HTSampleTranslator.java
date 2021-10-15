package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain; 
import org.jax.mgi.mgd.api.model.gxd.domain.HTSourceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapsDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPS;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

import org.jboss.logging.Logger;

public class HTSampleTranslator extends BaseEntityDomainTranslator<HTSample, HTSampleDomain> {
	
	@Override
	protected HTSampleDomain entityToDomain(HTSample entity) {

		Logger log = Logger.getLogger(getClass());
			
		HTSampleDomain domain = new HTSampleDomain();
		
		HTSampleDomain sampleDomain = new HTSampleDomain();

		// Sample Info
		sampleDomain.setName(entity.getName());
		sampleDomain.set_sample_key(entity.get_sample_key());
		sampleDomain.set_organism_key(entity.get_organism_key());
		sampleDomain.set_experiment_key(entity.get_experiment_key());
		sampleDomain.set_relevance_key(entity.get_relevance_key());
		sampleDomain.set_emapa_key(entity.get_emapa_key());
		sampleDomain.set_sex_key(entity.get_sex_key());
		sampleDomain.set_stage_key(entity.get_stage_key());
		sampleDomain.setAge(entity.getAge());

		return sampleDomain;
	}

}

















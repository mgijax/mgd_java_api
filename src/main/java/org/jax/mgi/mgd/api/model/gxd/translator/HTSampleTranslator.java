package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain; 
import org.jax.mgi.mgd.api.model.gxd.domain.HTSourceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapsDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
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

		// notes
		if (entity.getNotes() != null && entity.getNotes().size() > 0) {

			List<HTNoteDomain> noteList = new ArrayList<HTNoteDomain>();
			HTNoteDomain hTNoteDomain = new HTNoteDomain();

			String notetext = entity.getNotes().get(0).getNoteChunk().getNote();
			hTNoteDomain.setText(notetext);
			noteList.add(hTNoteDomain);
			sampleDomain.setNotes(noteList);
		}

		// Handling of genotype data
		if (entity.getGenotype() != null) {
			Genotype genotype = entity.getGenotype();
			HTGenotypeDomain genotypeDomain = new HTGenotypeDomain();
			genotypeDomain.set_genotype_key(genotype.get_genotype_key());
			genotypeDomain.setGeneticbackground(genotype.getStrain().getStrain());
			if (genotype.getMgiAccessionIds() != null && !genotype.getMgiAccessionIds().isEmpty()) {
				genotypeDomain.setMgiid(genotype.getMgiAccessionIds().get(0).getAccID());
			}
			sampleDomain.setGenotype_object(genotypeDomain);
		}

		// Handling of EMAPS / EMAPS terms
		if (entity.getEmapaObject() != null) {

			HTEmapaDomain hTEmapaDomain = new HTEmapaDomain();
			HTEmapsDomain hTEmapsDomain = new HTEmapsDomain();
			Term emapaTerm = entity.getEmapaTerm();
			TermEMAPA emapaObject = entity.getEmapaObject();

			hTEmapaDomain.set_term_key(emapaTerm.get_term_key());
			hTEmapaDomain.setTerm(emapaTerm.getTerm());
			hTEmapsDomain.set_stage_key(entity.get_stage_key());
			hTEmapsDomain.set_emapa_term_key(emapaTerm.get_term_key());
			// find emaps as for given emapa & stage
			for (TermEMAPS  termEMAPS: emapaObject.getEmapsTerms()) {
				if (termEMAPS.get_stage_key() == entity.get_stage_key()) {
					hTEmapsDomain.setPrimaryid(termEMAPS.getTerm().getAccessionIds().get(0).getAccID());
					hTEmapsDomain.set_term_key(termEMAPS.getTerm().get_term_key());
				}
			}
			hTEmapsDomain.setEmapa_term(hTEmapaDomain);
			sampleDomain.setEmaps_object(hTEmapsDomain);
		}

		return sampleDomain;
	}

}
















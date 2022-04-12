package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTEmapsDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPS;
import org.jax.mgi.mgd.api.util.Constants;

public class HTSampleTranslator extends BaseEntityDomainTranslator<HTSample, HTSampleDomain> {
	
	@Override
	protected HTSampleDomain entityToDomain(HTSample entity) {

		//Logger log = Logger.getLogger(getClass());
		
		HTSampleDomain sampleDomain = new HTSampleDomain();

		// Sample Info
		sampleDomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		sampleDomain.set_sample_key(entity.get_sample_key());
		sampleDomain.set_experiment_key(entity.get_experiment_key());
		sampleDomain.setName(entity.getName());	
		sampleDomain.setAge(entity.getAge());

		// null not allowed
		if (entity.getOrganism() != null) {
			sampleDomain.set_organism_key(entity.getOrganism().get_organism_key());
		}
		
		if (entity.getRelevance() != null) {
			sampleDomain.set_relevance_key(entity.getRelevance().get_term_key());
		}
		
		if (entity.getSex() != null) {
			sampleDomain.set_sex_key(entity.getSex().get_term_key());
		}

		if (entity.getCellTypeTerm() != null) {
			sampleDomain.set_celltype_term_key(entity.getCellTypeTerm().get_term_key());
		}
		
		// null allowed
		if (entity.getTheilerStage() != null) {
			sampleDomain.set_stage_key(entity.getTheilerStage().get_stage_key());
		}
		
		// Handling of genotype data/should not be null
		if (entity.getGenotype() != null) {
			Genotype genotype = entity.getGenotype();
			HTGenotypeDomain genotypeDomain = new HTGenotypeDomain();
			sampleDomain.set_genotype_key(genotype.get_genotype_key());
			genotypeDomain.set_genotype_key(genotype.get_genotype_key());
			genotypeDomain.set_strain_key(genotype.getStrain().get_strain_key());
			genotypeDomain.setIsConditional(genotype.getIsConditional());
			genotypeDomain.setGeneticbackground(genotype.getStrain().getStrain());
			if (genotype.getAlleleDetailNote() != null && !genotype.getAlleleDetailNote().isEmpty()) {
				genotypeDomain.setCombination1_cache(genotype.getAlleleDetailNote().get(0).getNote());
			}
			if (genotype.getMgiAccessionIds() != null && !genotype.getMgiAccessionIds().isEmpty()) {
				genotypeDomain.setMgiid(genotype.getMgiAccessionIds().get(0).getAccID());
			}
			sampleDomain.setGenotype_object(genotypeDomain);
		}

		// Handling of EMAPS / EMAPS terms/may be null
		if (entity.getEmapaObject() != null && entity.getEmapaTerm() != null) {

			HTEmapaDomain hTEmapaDomain = new HTEmapaDomain();
			HTEmapsDomain hTEmapsDomain = new HTEmapsDomain();
			Term emapaTerm = entity.getEmapaTerm();
			TermEMAPA emapaObject = entity.getEmapaObject();
			sampleDomain.set_emapa_key(emapaTerm.get_term_key());
			hTEmapaDomain.set_term_key(emapaTerm.get_term_key());
			hTEmapaDomain.setTerm(emapaTerm.getTerm());
			hTEmapsDomain.set_stage_key(entity.getTheilerStage().get_stage_key());
			hTEmapsDomain.set_emapa_term_key(emapaTerm.get_term_key());

			// find emaps as for given emapa & stage
			for (TermEMAPS  termEMAPS: emapaObject.getEmapsTerms()) {
				if (termEMAPS.get_stage_key() == sampleDomain.get_stage_key()) {
					hTEmapsDomain.setPrimaryid(termEMAPS.getTerm().getAccessionIds().get(0).getAccID());
					hTEmapsDomain.set_term_key(termEMAPS.getTerm().get_term_key());
				}
			}
			
			hTEmapsDomain.setEmapa_term(hTEmapaDomain);
			sampleDomain.setEmaps_object(hTEmapsDomain);
		}

		// Handling of Cell Type terms/may be null
		if (entity.getCellTypeTerm() != null) {
			HTCellTypeDomain clDomain = new HTCellTypeDomain();
			sampleDomain.set_celltype_term_key(entity.getCellTypeTerm().get_term_key());
			clDomain.set_term_key(entity.getCellTypeTerm().get_term_key());
			clDomain.setTerm(entity.getCellTypeTerm().getTerm());
			clDomain.setAbbreviation(entity.getCellTypeTerm().getAbbreviation());
			sampleDomain.setCl_object(clDomain);
		}
		else {
			sampleDomain.setCl_object(null);
		}

		
		// notes using HTNoteDomain
		if (entity.getNotes() != null && entity.getNotes().size() > 0) {

			List<HTNoteDomain> noteList = new ArrayList<HTNoteDomain>();
			HTNoteDomain hTNoteDomain = new HTNoteDomain();

			String notetext = entity.getNotes().get(0).getNote();
			hTNoteDomain.setText(notetext);
			noteList.add(hTNoteDomain);
			sampleDomain.setNotes(noteList);
		}

		// notes using NoteDomain due to API processing
		if (entity.getNotes() != null && !entity.getNotes().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNotes());
			sampleDomain.setHtNotes(note.iterator().next());
		}
		
		return sampleDomain;
	}

}

















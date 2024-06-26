package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelBandDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelLaneTranslator extends BaseEntityDomainTranslator<GelLane, GelLaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GelLaneDomain entityToDomain(GelLane entity) {

		GelLaneDomain domain = new GelLaneDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setGelLaneKey(String.valueOf(entity.get_gellane_key()));
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setGelRNATypeKey(String.valueOf(entity.getGelRNAType().get_term_key()));
		domain.setGelRNAType(entity.getGelRNAType().getTerm());
		domain.setGelControlKey(String.valueOf(entity.getGelControl().get_term_key()));
		domain.setGelControl(entity.getGelControl().getTerm());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setLaneLabel(entity.getLaneLabel());
		domain.setSampleAmount(entity.getSampleAmount());
		domain.setSex(entity.getSex());
		domain.setAgeMin(String.valueOf(entity.getAgeMin()));
		domain.setAgeMax(String.valueOf(entity.getAgeMax()));
		domain.setAgeNote(entity.getAgeNote());
		domain.setLaneNote(entity.getLaneNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// genotype stuff 
		
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setGenotypeAccID(entity.getGenotype().getMgiAccessionIds().get(0).getAccID());
		domain.setGenotypeBackground(entity.getGenotype().getStrain().getStrain());
		domain.setGenotypeIsConditional(String.valueOf(entity.getGenotype().getIsConditional()));
		if (entity.getGenotype().getAlleleDetailNote() != null && !entity.getGenotype().getAlleleDetailNote().isEmpty()) {
			domain.setGenotypeAllelePairs(entity.getGenotype().getAlleleDetailNote().get(0).getNote());
		}
		
		// age stuff
		
		domain.setAge(entity.getAge());
		String age = domain.getAge();
		
		if (age.equals("Not Applicable")
				|| age.equals("Not Loaded")
				|| age.equals("Not Resolved")
				|| age.equals("Not Specified")
				|| age.equals("embryonic")
				|| age.equals("embryonic brain")
				|| age.equals("postnatal")
				|| age.equals("postnatal adult")
				|| age.equals("postnatal newborn")
				) {
			domain.setAgePrefix(age);
		}
		else {		
			List<String> ageList = new ArrayList<String>(Arrays.asList(age.split(" ")));
			domain.setAgePrefix(ageList.get(0) + " " + ageList.get(1));
			String ageStage = "";
			for (int i = 2; i < ageList.size(); i++) {
				ageStage = ageStage + ageList.get(i);
			}
			domain.setAgeStage(ageStage);			
		}
		
		// end age stuff
		
		// structures
		if (entity.getStructures() != null && !entity.getStructures().isEmpty()) {
			GelLaneStructureTranslator structureTranslator = new GelLaneStructureTranslator();
			Iterable<GelLaneStructureDomain> i = structureTranslator.translateEntities(entity.getStructures());
			domain.setStructures(IteratorUtils.toList(i.iterator()));
			domain.getStructures().sort(Comparator.comparing(GelLaneStructureDomain::getEmapaTerm));
			domain.setStructuresCount(domain.getStructures().size());			
		}
		else {
			domain.setStructuresCount(0);
		}

		// gel bands
		if (entity.getGelBands() != null && !entity.getGelBands().isEmpty()) {
			GelBandTranslator bandTranslator = new GelBandTranslator();
			Iterable<GelBandDomain> i = bandTranslator.translateEntities(entity.getGelBands());
			domain.setGelBands(IteratorUtils.toList(i.iterator()));
			domain.getGelBands().sort(Comparator.comparingInt(GelBandDomain::getSequenceNum));			
		}
		else {
			List<GelBandDomain> gelBands = new ArrayList<GelBandDomain>();
			GelBandDomain gelBand = new GelBandDomain();
			gelBand.setProcessStatus(Constants.PROCESS_CREATE);
			gelBand.setGelLaneKey(String.valueOf(entity.get_gellane_key()));
			gelBand.setAssayKey(String.valueOf(entity.get_assay_key()));
			gelBand.setGelRowKey("");
			gelBand.setGelBandKey("");
			gelBand.setBandNote("");
			
			// if gel control != No, default = Not Applicable
			if (!domain.getGelControl().equals("No")) {
				gelBand.setStrengthKey("107080651");
				gelBand.setStrength("Not Applicable");				
			}
			else {
				gelBand.setStrengthKey("");
				gelBand.setStrength("");
			}
			
			gelBands.add(gelBand);
			domain.setGelBands(gelBands);
		}
		
		return domain;
	}

}

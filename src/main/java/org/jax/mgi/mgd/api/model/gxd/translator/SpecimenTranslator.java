package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class SpecimenTranslator extends BaseEntityDomainTranslator<Specimen, SpecimenDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SpecimenDomain entityToDomain(Specimen entity) {

		SpecimenDomain domain = new SpecimenDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSpecimenKey(String.valueOf(entity.get_specimen_key()));		
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setEmbeddingKey(String.valueOf(entity.getEmbeddingMethod().get_embedding_key()));
		domain.setEmbeddingMethod(entity.getEmbeddingMethod().getEmbeddingMethod());
		domain.setFixationKey(String.valueOf(entity.getFixationMethod().get_fixation_key()));
		domain.setFixationMethod(entity.getFixationMethod().getFixation());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setSpecimenLabel(entity.getSpecimenLabel());
		domain.setSex(entity.getSex());
		domain.setAgeMin(String.valueOf(entity.getAgeMin()));
		domain.setAgeMax(String.valueOf(entity.getAgeMax()));
		domain.setAgeNote(entity.getAgeNote());
		domain.setHybridization(entity.getHybridization());
		domain.setSpecimenNote(entity.getSpecimenNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// genotype stuff 
		
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setGenotypeAccID(entity.getGenotype().getMgiAccessionIds().get(0).getAccID());
		domain.setGenotypeBackground(entity.getGenotype().getStrain().getStrain());

		List<String> allelePairList = new ArrayList<String>();
		for (int p = 0; p < entity.getGenotype().getAllelePairs().size(); p++) {
			if (!allelePairList.contains(entity.getGenotype().getAllelePairs().get(p).getAllele1().getSymbol())) {
				allelePairList.add(entity.getGenotype().getAllelePairs().get(p).getAllele1().getSymbol());
			}
		}
		String genotypeAllelePairs = String.join(",", allelePairList);
		domain.setGenotypeAllelePairs(genotypeAllelePairs);
		
		// end genotype stuff
		
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
		
		// results
		if (entity.getResults() != null && !entity.getResults().isEmpty()) {
			InSituResultTranslator resultTranslator = new InSituResultTranslator();
			Iterable<InSituResultDomain> i = resultTranslator.translateEntities(entity.getResults());
			domain.setSresults(IteratorUtils.toList(i.iterator()));
			domain.getSresults().sort(Comparator.comparingInt(InSituResultDomain::getSequenceNum));
			domain.setSresultsCount(domain.getSresults().size());			
		}
		
		return domain;
	}

}

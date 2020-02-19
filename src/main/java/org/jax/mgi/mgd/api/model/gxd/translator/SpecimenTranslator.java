package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Comparator;

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
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setGenotypeID(entity.getGenotype().getMgiAccessionIds().get(0).getAccID());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setSpecimenLabel(entity.getSpecimenLabel());
		domain.setSex(entity.getSex());
		domain.setAge(entity.getAge());
		domain.setAgeMin(String.valueOf(entity.getAgeMin()));
		domain.setAgeMax(String.valueOf(entity.getAgeMax()));
		domain.setAgeNote(entity.getAgeNote());
		domain.setHybridization(entity.getHybridization());
		domain.setSpecimenNote(entity.getSpecimenNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// results
		if (entity.getResults() != null && !entity.getResults().isEmpty()) {
			InSituResultTranslator resultTranslator = new InSituResultTranslator();
			Iterable<InSituResultDomain> i = resultTranslator.translateEntities(entity.getResults());
			domain.setResults(IteratorUtils.toList(i.iterator()));
			domain.getResults().sort(Comparator.comparingInt(InSituResultDomain::getSequenceNum));
		}
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultImageDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class InSituResultTranslator extends BaseEntityDomainTranslator<InSituResult, InSituResultDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected InSituResultDomain entityToDomain(InSituResult entity) {

		InSituResultDomain domain = new InSituResultDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setResultKey(String.valueOf(entity.get_result_key()));				
		domain.setSpecimenKey(String.valueOf(entity.get_specimen_key()));		
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setStrengthKey(String.valueOf(entity.getStrength().get_strength_key()));
		domain.setStrength(entity.getStrength().getStrength());
		domain.setPatternKey(String.valueOf(entity.getPattern().get_pattern_key()));
		domain.setPattern(entity.getPattern().getPattern());
		domain.setResultNote(entity.getResultNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// structures
		if (entity.getStructures() != null && !entity.getStructures().isEmpty()) {
			InSituResultStructureTranslator structureTranslator = new InSituResultStructureTranslator();
			Iterable<InSituResultStructureDomain> i = structureTranslator.translateEntities(entity.getStructures());
			domain.setStructures(IteratorUtils.toList(i.iterator()));
			domain.getStructures().sort(Comparator.comparing(InSituResultStructureDomain::getEmapaTerm));
		}

		// images
		if (entity.getImagePanes() != null && !entity.getImagePanes().isEmpty()) {
			InSituResultImageTranslator imageTranslator = new InSituResultImageTranslator();
			Iterable<InSituResultImageDomain> i = imageTranslator.translateEntities(entity.getImagePanes());
			domain.setImagePanes(IteratorUtils.toList(i.iterator()));
			domain.getImagePanes().sort(Comparator.comparing(InSituResultImageDomain::getImagePane));
		}
		
		return domain;
	}

}

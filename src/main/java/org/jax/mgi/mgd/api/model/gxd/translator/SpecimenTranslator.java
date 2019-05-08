package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;

public class SpecimenTranslator extends BaseEntityDomainTranslator<Specimen, SpecimenDomain> {

	private InSituResultTranslator insituResultTranslator = new InSituResultTranslator();

	@Override
	protected SpecimenDomain entityToDomain(Specimen entity, int translationDepth) {
		
		SpecimenDomain domain = new SpecimenDomain();
		domain.set_specimen_key(entity.get_specimen_key());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setSpecimenLabel(entity.getSpecimenLabel());
		domain.setSex(entity.getSex());
		domain.setAge(entity.getAge());
		domain.setAgeMin(entity.getAgeMin());
		domain.setAgeMax(entity.getAgeMax());
		domain.setAgeNote(entity.getAgeNote());
        domain.setHybridization(entity.getHybridization());
        domain.setSpecimenNote(entity.getSpecimenNote());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			
			Iterable<InSituResultDomain> insituResults = insituResultTranslator.translateEntities(entity.getSpecimenResult(), translationDepth - 1);
			domain.setInsituResults(IteratorUtils.toList(insituResults.iterator()));
			
		}
		return domain;
	}

}

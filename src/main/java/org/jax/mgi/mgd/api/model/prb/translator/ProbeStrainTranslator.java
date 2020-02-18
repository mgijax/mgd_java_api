package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class ProbeStrainTranslator extends BaseEntityDomainTranslator<ProbeStrain, ProbeStrainDomain> {

	@Override
	protected ProbeStrainDomain entityToDomain(ProbeStrain entity) {
		
		ProbeStrainDomain domain = new ProbeStrainDomain();
		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());
		domain.setStandard(String.valueOf(entity.getStandard()));
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		domain.setGeneticBackground(String.valueOf(entity.getGeneticBackground()));
		domain.setSpeciesKey(String.valueOf(entity.getSpecies().get_term_key()));
		domain.setSpecies(entity.getSpecies().getTerm());
		domain.setStrainTypeKey(String.valueOf(entity.getStrainType().get_term_key()));
		domain.setStrainType(entity.getStrainType().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));	
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
				
		return domain;
	}

}

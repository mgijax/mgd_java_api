package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAlleleStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlleleStrain;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeAlleleStrainTranslator extends BaseEntityDomainTranslator<ProbeAlleleStrain, ProbeAlleleStrainDomain> {

	@Override
	protected ProbeAlleleStrainDomain entityToDomain(ProbeAlleleStrain entity) {
		
		ProbeAlleleStrainDomain domain = new ProbeAlleleStrainDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);	
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));		
		domain.setStrain(entity.getStrain().getStrain());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

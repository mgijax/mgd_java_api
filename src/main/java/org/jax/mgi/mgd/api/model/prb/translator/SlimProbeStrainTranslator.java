package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class SlimProbeStrainTranslator extends BaseEntityDomainTranslator<ProbeStrain, SlimProbeStrainDomain> {
	
	@Override
	protected SlimProbeStrainDomain entityToDomain(ProbeStrain entity) {
		
		SlimProbeStrainDomain domain = new SlimProbeStrainDomain();
		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));

		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());			
		}
		
		return domain;
	}

}

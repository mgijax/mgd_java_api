package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class SlimProbeStrainTranslator extends BaseEntityDomainTranslator<ProbeStrain, SlimProbeStrainDomain> {

	@Override
	protected SlimProbeStrainDomain entityToDomain(ProbeStrain entity, int translationDepth) {
		
		SlimProbeStrainDomain domain = new SlimProbeStrainDomain();
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());

		return domain;
	}

}

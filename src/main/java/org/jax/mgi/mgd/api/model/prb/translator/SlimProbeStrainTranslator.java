package org.jax.mgi.mgd.api.model.prb.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class SlimProbeStrainTranslator extends BaseEntityDomainTranslator<ProbeStrain, SlimProbeStrainDomain> {

	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected SlimProbeStrainDomain entityToDomain(ProbeStrain entity) {
		
		SlimProbeStrainDomain domain = new SlimProbeStrainDomain();
		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));

		// mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		return domain;
	}

}

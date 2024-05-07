package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainToolDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class SlimProbeStrainToolTranslator extends BaseEntityDomainTranslator<ProbeStrain, SlimProbeStrainToolDomain> {
	
	private ProbeStrainMarkerTranslator markerTranslator = new ProbeStrainMarkerTranslator();

	@Override
	protected SlimProbeStrainToolDomain entityToDomain(ProbeStrain entity) {
		
		SlimProbeStrainToolDomain domain = new SlimProbeStrainToolDomain();
				
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());
		
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		if (entity.getIsPrivate() == 1) {
			domain.setIsPrivateString("Yes");
		}
		else {
			domain.setIsPrivateString("No");
		}
		
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			Iterable<ProbeStrainMarkerDomain> t = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(t.iterator()));
			domain.getMarkers().sort(Comparator.comparing(ProbeStrainMarkerDomain::getQualifierTerm));
		}

		return domain;
	}

}

package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.ArrayList;
import java.util.List;

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
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
			domain.setSearchLogicaldb(entity.getMgiAccessionIds().get(0).getLogicaldb().getName());
			domain.setSearchLogicaldbKey(String.valueOf(entity.getMgiAccessionIds().get(0).getLogicaldb().get_logicaldb_key()));
		}
		
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			Iterable<ProbeStrainMarkerDomain> t = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(t.iterator()));

			List<String> alleleList = new ArrayList<String>();
			for (int p = 0; p < domain.getMarkers().size(); p++) {
				if (domain.getMarkers().get(p).getAlleleSymbol() != null && !domain.getMarkers().get(p).getAlleleSymbol().isEmpty()) {
					alleleList.add(domain.getMarkers().get(p).getAlleleSymbol());
				}
			}
			String alleleString = String.join(",", alleleList);
			domain.setAlleleString(alleleString);
		}
		else {
			domain.setAlleleString("");
		}
		
		return domain;
	}

}

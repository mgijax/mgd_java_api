package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

public class SlimProbeTranslator extends BaseEntityDomainTranslator<Probe, SlimProbeDomain> {

	@Override
	protected SlimProbeDomain entityToDomain(Probe entity) {
		
		SlimProbeDomain domain = new SlimProbeDomain();
		
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());		

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}		

		// probe source
		ProbeSourceTranslator probesourceTranslator = new ProbeSourceTranslator();
		domain.setProbeSource(probesourceTranslator.entityToDomain(entity.getProbeSource()));
		
		return domain;
	}
	
}

package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeSummaryDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

public class SlimProbeSummaryTranslator extends BaseEntityDomainTranslator<Probe, SlimProbeSummaryDomain> {

	@Override
	protected SlimProbeSummaryDomain entityToDomain(Probe entity) {
		
		SlimProbeSummaryDomain domain = new SlimProbeSummaryDomain();
		
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());		

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}		
		
		return domain;
	}
	
}

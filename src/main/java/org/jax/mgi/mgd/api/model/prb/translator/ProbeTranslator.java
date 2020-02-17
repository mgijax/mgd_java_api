package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

public class ProbeTranslator extends BaseEntityDomainTranslator<Probe, ProbeDomain> {

	@Override
	protected ProbeDomain entityToDomain(Probe entity) {
		
		ProbeDomain domain = new ProbeDomain();
		
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());
		domain.setDerivedFrom(String.valueOf(entity.getDerivedFrom()));
		domain.setPrimer1sequence(entity.getPrimer1sequence());
		domain.setPrimer2sequence(entity.getPrimer2sequence());
		domain.setRegionCovered(entity.getRegionCovered());
		domain.setInsertSite(entity.getInsertSite());
		domain.setInsertSize(entity.getInsertSize());
		domain.setProductSize(entity.getProductSize());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}

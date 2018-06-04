package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

public class ProbeTranslator extends BaseEntityDomainTranslator<Probe, ProbeDomain> {

	@Override
	protected ProbeDomain entityToDomain(Probe entity, int translationDepth) {
		
		ProbeDomain domain = new ProbeDomain();
		domain.set_probe_key(entity.get_probe_key());
		domain.setName(entity.getName());
		domain.setDerivedFrom(entity.getDerivedFrom());
		domain.setPrimer1sequence(entity.getPrimer1sequence());
		domain.setPrimer2sequence(entity.getPrimer2sequence());
		domain.setRegionCovered(entity.getRegionCovered());
		domain.setInsertSite(entity.getInsertSite());
		domain.setInsertSize(entity.getInsertSize());
		domain.setProductSize(entity.getProductSize());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Probe domainToEntity(ProbeDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antigen;

public class SlimAntigenTranslator extends BaseEntityDomainTranslator<Antigen, SlimAntigenDomain> {
	
	@Override
	protected SlimAntigenDomain entityToDomain(Antigen entity) {
			
		SlimAntigenDomain domain = new SlimAntigenDomain();
		
		domain.setAntigenKey(String.valueOf(entity.get_antigen_key()));
		domain.setAntigenName(entity.getAntigenName());
		
		return domain;
	}

}

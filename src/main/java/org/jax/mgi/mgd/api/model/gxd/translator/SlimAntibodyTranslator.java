package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;

public class SlimAntibodyTranslator extends BaseEntityDomainTranslator<Antibody, SlimAntibodyDomain> {
	
	@Override
	protected SlimAntibodyDomain entityToDomain(Antibody entity) {
			
		SlimAntibodyDomain domain = new SlimAntibodyDomain();
		
		domain.setAntibodyKey(String.valueOf(entity.get_antibody_key()));
		domain.setAntibodyName(entity.getAntibodyName());

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;

public class AntibodyTranslator extends BaseEntityDomainTranslator<Antibody, AntibodyDomain> {

	@Override
	protected AntibodyDomain entityToDomain(Antibody entity, int translationDepth) {
		
		AntibodyDomain domain = new AntibodyDomain();
		domain.set_antibody_key(entity.get_antibody_key());
		//domain.setCreatedBy(entity.getCreatedBy().getLogin());
		//domain.setModifiedBy(entity.getModifiedBy().getLogin());
		//domain.setCreation_date(entity.getCreation_date());
		//domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Antibody domainToEntity(AntibodyDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

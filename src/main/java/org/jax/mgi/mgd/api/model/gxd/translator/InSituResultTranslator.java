package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;

public class InSituResultTranslator extends BaseEntityDomainTranslator<InSituResult, InSituResultDomain> {

	@Override
	protected InSituResultDomain entityToDomain(InSituResult entity, int translationDepth) {
		
		InSituResultDomain domain = new InSituResultDomain();
		domain.set_result_key(entity.get_result_key());
		domain.setSequenceNum(entity.getSequenceNum());
        domain.setResultNote(entity.getResultNote());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
		}
		return domain;
	}

	@Override
	protected InSituResult domainToEntity(InSituResultDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

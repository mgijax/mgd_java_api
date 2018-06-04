package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class AssayTranslator extends BaseEntityDomainTranslator<Assay, AssayDomain> {

	@Override
	protected AssayDomain entityToDomain(Assay entity, int translationDepth) {
		
		AssayDomain domain = new AssayDomain();
		domain.set_assay_key(entity.get_assay_key());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Assay domainToEntity(AssayDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

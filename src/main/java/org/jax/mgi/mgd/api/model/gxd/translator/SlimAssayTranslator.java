package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class SlimAssayTranslator extends BaseEntityDomainTranslator<Assay, SlimAssayDomain> {
	
	@Override
	protected SlimAssayDomain entityToDomain(Assay entity) {
			
		SlimAssayDomain domain = new SlimAssayDomain();
		
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAssayDisplay(entity.getReference().getReferenceCitationCache().getJnumid() + ";" + entity.getAssayType().getAssayType() + ";" + entity.getMarker().getSymbol());
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));

		return domain;
	}

}

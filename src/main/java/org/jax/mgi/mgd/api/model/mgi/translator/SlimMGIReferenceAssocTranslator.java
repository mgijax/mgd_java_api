package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMGIReferenceAssocTranslator extends BaseEntityDomainTranslator<MGIReferenceAssoc, SlimMGIReferenceAssocDomain> {

	@Override
	protected SlimMGIReferenceAssocDomain entityToDomain(MGIReferenceAssoc entity) {
		SlimMGIReferenceAssocDomain domain = new SlimMGIReferenceAssocDomain();
		
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setRefAssocTypeKey(String.valueOf(entity.getRefAssocType().get_refAssocType_key()));
	    domain.setRefAssocType(entity.getRefAssocType().getAssocType());
	    
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
			
		return domain;
	}

}

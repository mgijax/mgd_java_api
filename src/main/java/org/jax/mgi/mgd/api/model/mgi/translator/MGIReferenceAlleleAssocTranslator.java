package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAlleleAssocTranslator extends BaseEntityDomainTranslator<MGIReferenceAssoc, MGIReferenceAlleleAssocDomain> {

	@Override
	protected MGIReferenceAlleleAssocDomain entityToDomain(MGIReferenceAssoc entity) {
		MGIReferenceAlleleAssocDomain domain = new MGIReferenceAlleleAssocDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setRefAssocTypeKey(String.valueOf(entity.getRefAssocType().get_refAssocType_key()));
	    domain.setRefAssocType(entity.getRefAssocType().getAssocType());
	    
//		these fields are gotten via call to MGIReferenceService.getAlleles()
//		private String alleleSymbol;
//		private String alleleAccID;
//		private String alleleMarkerSymbol;
		
		return domain;
	}

}

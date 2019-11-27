package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAssocTranslator extends BaseEntityDomainTranslator<MGIReferenceAssoc, MGIReferenceAssocDomain> {

	@Override
	protected MGIReferenceAssocDomain entityToDomain(MGIReferenceAssoc entity) {
		MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setRefAssocTypeKey(String.valueOf(entity.getRefAssocType().get_refAssocType_key()));
	    domain.setRefAssocType(entity.getRefAssocType().getAssocType());
	    domain.setAllowOnlyOne(String.valueOf(entity.getRefAssocType().getAllowOnlyOne()));

		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
			
		return domain;
	}

}

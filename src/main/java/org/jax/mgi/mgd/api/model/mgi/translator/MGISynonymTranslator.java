package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISynonymTranslator extends BaseEntityDomainTranslator<MGISynonym, MGISynonymDomain> {

	@Override
	protected MGISynonymDomain entityToDomain(MGISynonym entity) {
		MGISynonymDomain domain = new MGISynonymDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSynonymKey(String.valueOf(entity.get_synonym_key()));
		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setSynonymTypeKey(String.valueOf(entity.getSynonymType().get_synonymType_key()));
	    domain.setSynonymType(entity.getSynonymType().getSynonymType());
	    
		domain.setSynonym(entity.getSynonym());

		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getJnumid());
			domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
			domain.setShort_citation(entity.getReference().getShort_citation());
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISynonymTranslator extends BaseEntityDomainTranslator<MGISynonym, MGISynonymDomain> {

	@Override
	protected MGISynonymDomain entityToDomain(MGISynonym entity, int translationDepth) {
		MGISynonymDomain domain = new MGISynonymDomain();
		domain.setSynonymKey(entity.get_synonym_key());
		domain.set_object_key(entity.get_object_key());
		domain.setSynonym(entity.getSynonym());
	    domain.setMgiType(entity.getMgiType().getName());
	    domain.setSynonymType(entity.getSynonymType().getSynonymType());
	    
		//if ( entity.getReference().getJnumid() != null ) {
		//	domain.setJNum(entity.getReference().getJnumid());
		//}
	    
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected MGISynonym domainToEntity(MGISynonymDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

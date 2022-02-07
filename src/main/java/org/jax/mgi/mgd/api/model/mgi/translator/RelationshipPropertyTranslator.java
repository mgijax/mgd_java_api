package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipProperty;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipPropertyTranslator extends BaseEntityDomainTranslator<RelationshipProperty, RelationshipPropertyDomain> {
		
	@Override
	protected RelationshipPropertyDomain entityToDomain(RelationshipProperty entity) {	
		RelationshipPropertyDomain domain = new RelationshipPropertyDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRelationshipPropertyKey(String.valueOf(entity.get_relationshipproperty_key()));
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setPropertyNameKey(String.valueOf(entity.getPropertyName().get_term_key()));
		domain.setPropertyName(entity.getPropertyName().getTerm());
		domain.setValue(entity.getValue());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
						
		return domain;
	}

}

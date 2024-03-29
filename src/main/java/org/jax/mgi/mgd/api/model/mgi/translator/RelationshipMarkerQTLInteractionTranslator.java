package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerQTLInteractionDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipMarkerQTLInteraction;

public class RelationshipMarkerQTLInteractionTranslator extends BaseEntityDomainTranslator<RelationshipMarkerQTLInteraction, RelationshipMarkerQTLInteractionDomain> {
		
	@Override
	protected RelationshipMarkerQTLInteractionDomain entityToDomain(RelationshipMarkerQTLInteraction entity) {	
		RelationshipMarkerQTLInteractionDomain domain = new RelationshipMarkerQTLInteractionDomain();

		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setObjectKey2(String.valueOf(entity.get_object_key_2()));
		domain.setSymbol1(entity.getMarker1());
		domain.setSymbol2(entity.getMarker2());
		
		return domain;
	}

}

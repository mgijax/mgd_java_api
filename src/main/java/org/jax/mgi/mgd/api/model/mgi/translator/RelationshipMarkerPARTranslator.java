package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerPARDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipMarkerPAR;

public class RelationshipMarkerPARTranslator extends BaseEntityDomainTranslator<RelationshipMarkerPAR, RelationshipMarkerPARDomain> {
		
	@Override
	protected RelationshipMarkerPARDomain entityToDomain(RelationshipMarkerPAR entity) {	
		RelationshipMarkerPARDomain domain = new RelationshipMarkerPARDomain();

		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setSymbol(entity.getMarker());
		
		return domain;
	}

}

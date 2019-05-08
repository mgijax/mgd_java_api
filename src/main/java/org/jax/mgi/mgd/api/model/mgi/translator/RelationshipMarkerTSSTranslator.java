package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerTSSDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipMarkerTSS;

public class RelationshipMarkerTSSTranslator extends BaseEntityDomainTranslator<RelationshipMarkerTSS, RelationshipMarkerTSSDomain> {
		
	@Override
	protected RelationshipMarkerTSSDomain entityToDomain(RelationshipMarkerTSS entity) {	
		RelationshipMarkerTSSDomain domain = new RelationshipMarkerTSSDomain();

		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setObjectKey2(String.valueOf(entity.get_object_key_2()));
		domain.setSymbol1(entity.getMarker1());
		domain.setSymbol2(entity.getMarker2());
		
		return domain;
	}

}

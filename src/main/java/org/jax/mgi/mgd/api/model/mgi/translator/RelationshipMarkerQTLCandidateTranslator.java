package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerQTLCandidateDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipMarkerQTLCandidate;

public class RelationshipMarkerQTLCandidateTranslator extends BaseEntityDomainTranslator<RelationshipMarkerQTLCandidate, RelationshipMarkerQTLCandidateDomain> {
		
	@Override
	protected RelationshipMarkerQTLCandidateDomain entityToDomain(RelationshipMarkerQTLCandidate entity) {	
		RelationshipMarkerQTLCandidateDomain domain = new RelationshipMarkerQTLCandidateDomain();

		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setObjectKey2(String.valueOf(entity.get_object_key_2()));
		domain.setSymbol1(entity.getMarker1());
		domain.setSymbol2(entity.getMarker2());
		domain.setJnumid(entity.getJnumid());
		
		return domain;
	}

}

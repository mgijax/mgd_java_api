package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFEARDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipFEAR;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipFEARTranslator extends BaseEntityDomainTranslator<RelationshipFEAR, RelationshipFEARDomain> {
		
	@Override
	protected RelationshipFEARDomain entityToDomain(RelationshipFEAR entity) {	
		RelationshipFEARDomain domain = new RelationshipFEARDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setMarkerSymbol(entity.getSymbol());	
		domain.setCommonname(entity.getCommonname());
		domain.setAccID(entity.getAccID());
		
		// properties
//		if (entity.getProperties() != null) {
//			RelationshipPropertyTranslator propertyTranslator = new RelationshipPropertyTranslator();
//			Iterable<RelationshipPropertyDomain> i = propertyTranslator.translateEntities(entity.getProperties());
//			domain.setProperties(IteratorUtils.toList(i.iterator()));
//		}
		
		return domain;
	}

}

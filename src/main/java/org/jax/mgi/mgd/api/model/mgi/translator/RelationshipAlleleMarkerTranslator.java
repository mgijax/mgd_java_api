package org.jax.mgi.mgd.api.model.mgi.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipAlleleMarkerDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipAlleleMarker;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipAlleleMarkerTranslator extends BaseEntityDomainTranslator<RelationshipAlleleMarker, RelationshipAlleleMarkerDomain> {
		
	@Override
	protected RelationshipAlleleMarkerDomain entityToDomain(RelationshipAlleleMarker entity) {	
		RelationshipAlleleMarkerDomain domain = new RelationshipAlleleMarkerDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setMarkerSymbol(entity.getSymbol());	
		domain.setCommonname(entity.getCommonname());
		//domain.setAccID(entity.getAccID());
		
		// properties
//		if (entity.getProperties() != null) {
//			RelationshipPropertyTranslator propertyTranslator = new RelationshipPropertyTranslator();
//			Iterable<RelationshipPropertyDomain> i = propertyTranslator.translateEntities(entity.getProperties());
//			domain.setProperties(IteratorUtils.toList(i.iterator()));
//		}
		
		return domain;
	}

}

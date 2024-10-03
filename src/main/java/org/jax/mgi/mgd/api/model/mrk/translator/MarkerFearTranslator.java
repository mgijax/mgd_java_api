package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByMarkerDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipFearByMarkerTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jboss.logging.Logger;

public class MarkerFearTranslator extends BaseEntityDomainTranslator<Marker, MarkerFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected MarkerFearDomain entityToDomain(Marker entity) {
		
		MarkerFearDomain domain = new MarkerFearDomain();
		RelationshipFearByMarkerTranslator fearTranslator = new RelationshipFearByMarkerTranslator();	

		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// relationship domain by marker/cluster_has_member
		if (entity.getClusterHasMember() != null && !entity.getClusterHasMember().isEmpty()) {
			Iterable<RelationshipFearByMarkerDomain> t = fearTranslator.translateEntities(entity.getClusterHasMember());			
			domain.setClusterHasMember(IteratorUtils.toList(t.iterator()));
			domain.getClusterHasMember().sort(Comparator.comparing(RelationshipFearByMarkerDomain::getMarkerSymbol2, String.CASE_INSENSITIVE_ORDER));
		}
		
		// relationship domain by marker/regulates_exrepssion
		if (entity.getRegulatesExpression() != null && !entity.getRegulatesExpression().isEmpty()) {
			Iterable<RelationshipFearByMarkerDomain> t = fearTranslator.translateEntities(entity.getRegulatesExpression());			
			domain.setRegulatesExpression(IteratorUtils.toList(t.iterator()));
			domain.getRegulatesExpression().sort(Comparator.comparing(RelationshipFearByMarkerDomain::getMarkerSymbol2, String.CASE_INSENSITIVE_ORDER));	
		}
		
		return domain;
	}

}

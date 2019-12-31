package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class MarkerAnnotTranslator extends BaseEntityDomainTranslator<Marker, MarkerAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AnnotationTranslator annotTranslator = new AnnotationTranslator();	
	
	@Override
	protected MarkerAnnotDomain entityToDomain(Marker entity) {
		
		MarkerAnnotDomain domain = new MarkerAnnotDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerDisplay(entity.getSymbol() + ", " + entity.getName());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// GO annotations by marker
		if (entity.getGoAnnots() != null && !entity.getGoAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getGoAnnots());			
			domain.setAnnots(IteratorUtils.toList(t.iterator()));
			domain.getAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));	
		}
			
		return domain;
	}

}

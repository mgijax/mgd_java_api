package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.GOTrackingDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class MarkerAnnotTranslator extends BaseEntityDomainTranslator<Marker, MarkerAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected MarkerAnnotDomain entityToDomain(Marker entity) {
		
		MarkerAnnotDomain domain = new MarkerAnnotDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerDisplay(entity.getSymbol() + ", " + entity.getName());
		domain.setMarkerStatusKey(entity.getMarkerStatus().get_marker_status_key().toString());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());		
		domain.setMarkerTypeKey(String.valueOf(entity.getMarkerType().get_marker_type_key()));
		domain.setMarkerType(entity.getMarkerType().getName());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		// notes
		if (entity.getGoNote() != null && !entity.getGoNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGoNote());
			domain.setGoNote(IteratorUtils.toList(note.iterator()));
		}
		
		// GO annotations by marker
		if (entity.getGoAnnots() != null && !entity.getGoAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();	
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getGoAnnots());			
			domain.setAnnots(IteratorUtils.toList(t.iterator()));
			domain.getAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));	
		}

		// GO tracking
		if (entity.getGoTracking() != null && !entity.getGoTracking().isEmpty()) {
			GOTrackingTranslator goTrackingTranslator = new GOTrackingTranslator();
			Iterable<GOTrackingDomain> i = goTrackingTranslator.translateEntities(entity.getGoTracking());
			domain.setGoTracking(IteratorUtils.toList(i.iterator()));
		}
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class SlimMarkerTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerDomain> {
	
	@Override
	protected SlimMarkerDomain entityToDomain(Marker entity) {
			
		SlimMarkerDomain domain = new SlimMarkerDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());

		// at most one detailClipNote
		if (entity.getDetailClipNote() != null && !entity.getDetailClipNote().isEmpty()) {
			MarkerNoteTranslator markerNoteTranslator = new MarkerNoteTranslator();
			Iterable<MarkerNoteDomain> clipNote = markerNoteTranslator.translateEntities(entity.getDetailClipNote());
			domain.setDetailClipNote(clipNote.iterator().next());
		}
		
		return domain;
	}

}

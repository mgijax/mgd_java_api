package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;

public class MarkerNoteTranslator extends BaseEntityDomainTranslator<MarkerNote, MarkerNoteDomain> {

	@Override
	protected MarkerNoteDomain entityToDomain(MarkerNote entity) {
		MarkerNoteDomain domain = new MarkerNoteDomain();
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setNote(entity.getNote());
//		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
//		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

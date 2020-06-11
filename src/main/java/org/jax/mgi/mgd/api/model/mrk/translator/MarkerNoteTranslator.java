package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;
import org.jax.mgi.mgd.api.util.Constants;

public class MarkerNoteTranslator extends BaseEntityDomainTranslator<MarkerNote, MarkerNoteDomain> {

	@Override
	protected MarkerNoteDomain entityToDomain(MarkerNote entity) {

		MarkerNoteDomain domain = new MarkerNoteDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setNote(entity.getNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeNotesDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNotes;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeNotesTranslator extends BaseEntityDomainTranslator<ProbeNotes, ProbeNotesDomain> {

	@Override
	protected ProbeNotesDomain entityToDomain(ProbeNotes entity) {
		
		ProbeNotesDomain domain = new ProbeNotesDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setNoteKey(String.valueOf(entity.get_note_key()));
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setNotes(String.valueOf(entity.getNotes()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

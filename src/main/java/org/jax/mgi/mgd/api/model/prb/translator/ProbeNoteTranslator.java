package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeNoteDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeNoteTranslator extends BaseEntityDomainTranslator<ProbeNote, ProbeNoteDomain> {

	@Override
	protected ProbeNoteDomain entityToDomain(ProbeNote entity) {
		
		ProbeNoteDomain domain = new ProbeNoteDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setNoteKey(String.valueOf(entity.get_note_key()));
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setNote(String.valueOf(entity.getNote()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReferenceNote;

public class ProbeReferenceNoteTranslator extends BaseEntityDomainTranslator<ProbeReferenceNote, ProbeReferenceNoteDomain> {

	@Override
	protected ProbeReferenceNoteDomain entityToDomain(ProbeReferenceNote entity) {
		
		ProbeReferenceNoteDomain domain = new ProbeReferenceNoteDomain();
		
		domain.setReferenceKey(String.valueOf(entity.get_reference_key()));
		domain.setNote(String.valueOf(entity.getNote()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

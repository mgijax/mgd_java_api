package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.MappingNoteDomain;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;
import org.jax.mgi.mgd.api.util.Constants;

public class MappingNoteTranslator extends BaseEntityDomainTranslator<MappingNote, MappingNoteDomain> {

	@Override
	protected MappingNoteDomain entityToDomain(MappingNote entity) {

		MappingNoteDomain domain = new MappingNoteDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setNote(entity.getNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

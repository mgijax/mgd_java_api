package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jboss.logging.Logger;

public class ReferenceNoteTranslator extends BaseEntityDomainTranslator<ReferenceNote, ReferenceNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected ReferenceNoteDomain entityToDomain(ReferenceNote entity) {

		ReferenceNoteDomain domain = new ReferenceNoteDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setNote(DecodeString.getDecodeToUTF8(entity.getNote()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

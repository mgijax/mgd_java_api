package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayNote;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AssayNoteTranslator extends BaseEntityDomainTranslator<AssayNote, AssayNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AssayNoteDomain entityToDomain(AssayNote entity) {

		AssayNoteDomain domain = new AssayNoteDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssayNoteKey(String.valueOf(entity.get_assaynote_key()));
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAssayNote(entity.getAssayNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

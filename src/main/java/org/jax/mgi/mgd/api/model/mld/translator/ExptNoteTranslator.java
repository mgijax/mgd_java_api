package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.ExptNoteDomain;
import org.jax.mgi.mgd.api.model.mld.entities.ExptNote;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ExptNoteTranslator extends BaseEntityDomainTranslator<ExptNote, ExptNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ExptNoteDomain entityToDomain(ExptNote entity) {
		
		ExptNoteDomain domain = new ExptNoteDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setNoteKey(entity.getNote());
		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setNote(entity.getNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}

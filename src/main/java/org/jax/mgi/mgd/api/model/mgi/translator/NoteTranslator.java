package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jboss.logging.Logger;

public class NoteTranslator extends BaseEntityDomainTranslator<Note, NoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected NoteDomain entityToDomain(Note entity) {
		NoteDomain domain = new NoteDomain();

		//domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setNoteKey(String.valueOf(entity.get_note_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setMgiType(entity.getMgiType().getName());
		domain.setNoteTypeKey(String.valueOf(entity.getNoteType().get_noteType_key()));
		domain.setNoteType(entity.getNoteType().getNoteType());
		domain.setNoteChunk(DecodeString.getDecodeToUTF8(entity.getNoteChunk().getNote()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

}

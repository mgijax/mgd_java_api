package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

public class NoteTranslator extends BaseEntityDomainTranslator<Note, NoteDomain> {

	@Override
	protected NoteDomain entityToDomain(Note entity, int translationDepth) {
		NoteDomain domain = new NoteDomain();

		domain.setNoteKey(String.valueOf(entity.get_note_key()));
		domain.setObjectKey(entity.get_object_key().toString());
		domain.setMgiTypeKey(entity.getMgiType().get_mgitype_key().toString());
		domain.setMgiType(entity.getMgiType().getName());
		domain.setNoteTypeKey(entity.getNoteType().get_noteType_key().toString());
		domain.setNoteType(entity.getNoteType().getNoteType());
		domain.setNoteChunk(entity.getNoteChunk().getNote());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

	@Override
	protected Note domainToEntity(NoteDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

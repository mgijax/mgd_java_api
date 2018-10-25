package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

public class NoteTranslator extends BaseEntityDomainTranslator<Note, NoteDomain> {

	@Override
	protected NoteDomain entityToDomain(Note entity, int translationDepth) {
		NoteDomain domain = new NoteDomain();

		domain.setNoteKey(entity.get_note_key());
		domain.set_object_key(entity.get_object_key());
		domain.setMgiType(entity.getMgiType().getName());
		domain.setNoteType(entity.getNoteType().getNoteType());
		domain.setNoteChunk(entity.getNoteChunk().getNote());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected Note domainToEntity(NoteDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

package org.jax.mgi.mgd.api.model.mgi.translator;

import java.io.UnsupportedEncodingException;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
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
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		String decodedToUTF8 = "";
		try {
			Boolean executeDecode = true;
			
			// decode postgres/latin9 to UTF8
			decodedToUTF8 = new String(entity.getNoteChunk().getNote().getBytes("ISO-8859-15"), "UTF-8");
			
			// if postgres contains characters that cannot be converted to UTF8
			// then use existing postgres note
			// else, use decoded UTF8 encoding
			for (int i = 0; i < decodedToUTF8.length(); i++){
				if (decodedToUTF8.codePointAt(i) == 65533) {
					executeDecode = false;
				}
			}
			
			if (executeDecode) {
				domain.setNoteChunk(decodedToUTF8);				
			}
			else {
				domain.setNoteChunk(entity.getNoteChunk().getNote());				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return domain;
	}

}

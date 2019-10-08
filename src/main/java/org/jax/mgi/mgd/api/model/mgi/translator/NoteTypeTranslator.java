package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.NoteType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoteTypeTranslator extends BaseEntityDomainTranslator<NoteType, NoteTypeDomain> {

	@Override
	protected NoteTypeDomain entityToDomain(NoteType entity) {
		NoteTypeDomain domain = new NoteTypeDomain();
		
		domain.setNoteTypeKey(String.valueOf(entity.get_noteType_key()));
		domain.setNoteType(entity.getNoteType());
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getMgiType() != null) {
			domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		}
		
		return domain;
	}

}

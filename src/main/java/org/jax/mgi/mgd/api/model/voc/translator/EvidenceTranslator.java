package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;
import org.jax.mgi.mgd.api.util.Constants;

public class EvidenceTranslator extends BaseEntityDomainTranslator<Evidence, EvidenceDomain> {
	
	private NoteTranslator noteTranslator = new NoteTranslator();		

	@Override
	protected EvidenceDomain entityToDomain(Evidence entity) {
		EvidenceDomain domain = new EvidenceDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotEvidenceKey(String.valueOf(entity.get_annotevidence_key()));
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));
		domain.setEvidenceTermKey(String.valueOf(entity.getEvidenceTerm().get_term_key()));
		domain.setEvidenceTerm(entity.getEvidenceTerm().getTerm());
		domain.setInferredFrom(entity.getInferredFrom());
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
			domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
			domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));		
	
		// at most one general note
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			domain.setGeneralNote(note.iterator().next());
		}
		
		// at most one background sensivity
		if (entity.getBackgroundSensitivityNote() != null && !entity.getBackgroundSensitivityNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getBackgroundSensitivityNote());
			domain.setBackgroundSensitivityNote(note.iterator().next());
		}
		
		// at most one normal note
		if (entity.getNormalNote() != null && !entity.getNormalNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNormalNote());
			domain.setNormalNote(note.iterator().next());
		}
				
		return domain;
	}

}

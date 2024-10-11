package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByAlleleDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipFearByAllele;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipFearByAlleleTranslator extends BaseEntityDomainTranslator<RelationshipFearByAllele, RelationshipFearByAlleleDomain> {
		
	@Override
	protected RelationshipFearByAlleleDomain entityToDomain(RelationshipFearByAllele entity) {	
		RelationshipFearByAlleleDomain domain = new RelationshipFearByAlleleDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setCategoryKey(String.valueOf(entity.get_category_key()));
		domain.setCategoryTerm(entity.getCategoryTerm());
		domain.setAlleleKey(String.valueOf(entity.get_object_key_1()));
		domain.setAlleleSymbol(entity.getAlleleSymbol());
		domain.setMarkerKey(String.valueOf(entity.get_object_key_2()));
		domain.setMarkerSymbol(entity.getMarkerSymbol());
		domain.setMarkerAccID(entity.getMarkerAccID());
		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setOrganism(entity.getOrganism());
		domain.setRelationshipTermKey(String.valueOf(entity.get_relationshipTerm_key()));
		domain.setRelationshipTerm(entity.getRelationshipTerm());
		domain.setQualifierKey(String.valueOf(entity.get_qualifier_key()));
		domain.setQualifierTerm(entity.getQualifierTerm());
		domain.setEvidenceKey(String.valueOf(entity.get_evidence_key()));
		domain.setEvidenceTerm(entity.getEvidenceTerm());
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setJnumid(entity.getJnumid());
		domain.setJnum(String.valueOf(entity.getJnum()));
		domain.setShort_citation(entity.getShort_citation());
		domain.setCreatedByKey(entity.get_createdby_key().toString());
		domain.setCreatedBy(entity.getCreatedBy());
		domain.setModifiedByKey(entity.get_modifiedby_key().toString());
		domain.setModifiedBy(entity.getModifiedBy());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// at most one note
		if (entity.getNote() != null && !entity.getNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNote());
			domain.setNote(note.iterator().next());
		}
		// create blank note
		else {
			NoteDomain noteDomain = new NoteDomain();
			noteDomain.setProcessStatus(Constants.PROCESS_CREATE);
			noteDomain.setMgiTypeKey("40");
			noteDomain.setNoteTypeKey("1042");
			noteDomain.setNoteKey("");
			noteDomain.setObjectKey("");			
			noteDomain.setNoteChunk("");
			domain.setNote(noteDomain);
		}
		
		return domain;
	}

}

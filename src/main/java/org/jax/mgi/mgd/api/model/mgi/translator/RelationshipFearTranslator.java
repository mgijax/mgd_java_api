package org.jax.mgi.mgd.api.model.mgi.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipFear;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipFearTranslator extends BaseEntityDomainTranslator<RelationshipFear, RelationshipFearDomain> {
		
	@Override
	protected RelationshipFearDomain entityToDomain(RelationshipFear entity) {	
		RelationshipFearDomain domain = new RelationshipFearDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setCategoryKey(String.valueOf(entity.get_category_key()));
		domain.setCategoryTerm(entity.getCategoryTerm());
		domain.setAlleleKey(String.valueOf(entity.get_object_key_1()));
		domain.setAlleleSymbol(entity.getAlleleSymbol());
		domain.setMarkerKey(String.valueOf(entity.get_object_key_2()));
		domain.setMarkerSymbol(entity.getMarkerSymbol());
		domain.setMarkerAccID(entity.getMarkerAccID());
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
		domain.setCreatedByKey(entity.get_createdby_key());
		domain.setCreatedBy(entity.getCreatedBy());
		domain.setModifiedByKey(entity.get_modifiedby_key());
		domain.setModifiedBy(entity.getModifiedBy());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date()); 
		
		// at most one note
		if (entity.getNote() != null && !entity.getNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNote());
			domain.setNote(note.iterator().next());
		}
		// create blank note
		else {
			NoteDomain noteDomain = new NoteDomain();
			noteDomain.setMgiTypeKey("40");
			noteDomain.setNoteTypeKey("1042");
			domain.setNote(noteDomain);
		}
		
		// properties
		if (entity.getProperties() != null) {
			RelationshipPropertyTranslator propertyTranslator = new RelationshipPropertyTranslator();
			Iterable<RelationshipPropertyDomain> i = propertyTranslator.translateEntities(entity.getProperties());
			domain.setProperties(IteratorUtils.toList(i.iterator()));
			domain.getProperties().sort(Comparator.comparingInt(RelationshipPropertyDomain::getSequenceNum));
		}
		
		return domain;
	}

}

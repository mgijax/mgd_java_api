package org.jax.mgi.mgd.api.model.mgi.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipTranslator extends BaseEntityDomainTranslator<Relationship, RelationshipDomain> {
		
	@Override
	protected RelationshipDomain entityToDomain(Relationship entity) {	
		RelationshipDomain domain = new RelationshipDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setObjectKey2(String.valueOf(entity.get_object_key_2()));

		// these are specific to the category/mgi-types
		// so a specific entity/domain is needed to map the category/mgi-type to its specific master.
		//domain.setObject1();
		//domain.setObject2();
		
		domain.setCategoryKey(String.valueOf(entity.getCategory().get_category_key()));
		domain.setCategoryTerm(entity.getCategory().getName());
		domain.setRelationshipTermKey(String.valueOf(entity.getRelationshipTerm().get_term_key()));
		domain.setRelationshipTerm(entity.getRelationshipTerm().getTerm());
		domain.setQualifierKey(String.valueOf(entity.getQualifierTerm().get_term_key()));
		domain.setQualifierTerm(entity.getQualifierTerm().getTerm());
		domain.setEvidenceKey(String.valueOf(entity.getEvidenceTerm().get_term_key()));
		domain.setEvidenceTerm(entity.getEvidenceTerm().getTerm());
		
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
		domain.setShort_citation(entity.getReference().getShort_citation());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
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
			noteDomain.setMgiTypeKey("40");
			noteDomain.setNoteTypeKey("1042");
			domain.setNote(noteDomain);
		}
		
		// properties
		if (entity.getProperties() != null) {
			RelationshipPropertyTranslator propertyTranslator = new RelationshipPropertyTranslator();
			Iterable<RelationshipPropertyDomain> i = propertyTranslator.translateEntities(entity.getProperties());
			domain.setProperties(IteratorUtils.toList(i.iterator()));
		}
		
		return domain;
	}

}

package org.jax.mgi.mgd.api.model.mgi.translator;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipTranslator extends BaseEntityDomainTranslator<Relationship, RelationshipDomain> {
	
	@Override
	protected RelationshipDomain entityToDomain(Relationship entity, int translationDepth) {	
		RelationshipDomain domain = new RelationshipDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		
		domain.setObjectKey1(String.valueOf(entity.get_object_key_1()));
		domain.setObjectKey2(String.valueOf(entity.get_object_key_2()));

		// these are specific to the category/mgi-types
		// so a specific service is needed to map the category/mgi-type to its specific master.
		// for example, see mgi/serivce/RelationshpService.java/markerTss.
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
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// these domains are only set by individual object endpoints
		//if (translationDepth > 0) {
		
			// gene-to-tss relationships
			//if (entity.getGeneToMarker() != null) {
			//	Iterable<RelatinshipDomain> geneToTss = relationshipTranslator.translateEntities(entity.getGeneToMarker());
			//	if(geneToTss.iterator().hasNext() == true) {
			//		domain.setGeneToMarker(IteratorUtils.toList(geneToTss.iterator()));
			//	}
			//}
			
			// tss-to-gene relationships
			//if (entity.getTssToMarker() != null) {
			//	Iterable<RelationshipDomain> tssToGene = relationshipTranslator.translateEntities(entity.getTssToMarker());
			//	if(tssToGene.iterator().hasNext() == true) {
			//		domain.setTssToMarker(IteratorUtils.toList(tssToGene.iterator()));
			//	}
			//}
			
		//}
		
		return domain;
	}

	@Override
	protected Relationship domainToEntity(RelationshipDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

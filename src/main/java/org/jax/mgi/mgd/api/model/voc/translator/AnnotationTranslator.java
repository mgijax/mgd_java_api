package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AnnotationTranslator extends BaseEntityDomainTranslator<Annotation, AnnotationDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
	
	private EvidenceTranslator evidenceTranslator = new EvidenceTranslator();
	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected AnnotationDomain entityToDomain(Annotation entity) {
		AnnotationDomain domain = new AnnotationDomain();
		
		//log.info("AnnotationDomain: " + entity.get_annot_key());
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));
		domain.setAnnotTypeKey(String.valueOf(entity.getAnnotType().get_annotType_key()));
		domain.setAnnotType(entity.getAnnotType().getName());
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		domain.setQualifierKey(String.valueOf(entity.getQualifier().get_term_key()));
		domain.setQualifierAbbreviation(String.valueOf(entity.getQualifier().getAbbreviation()));
		domain.setQualifier(entity.getQualifier().getTerm());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		if (entity.getEvidences() != null && !entity.getEvidences().isEmpty()) {
			Iterable<EvidenceDomain> evid = evidenceTranslator.translateEntities(entity.getEvidences());		
			domain.setEvidence(IteratorUtils.toList(evid.iterator()));
		}
		
 		if (entity.getMarkerFeatureTypeIds() != null && !entity.getMarkerFeatureTypeIds().isEmpty()) {
 			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
 			domain.setMarkerFeatureTypes(IteratorUtils.toList(acc.iterator()));
 		}
 		
 		if (entity.getAlleleVariantSOIds() != null && !entity.getAlleleVariantSOIds().isEmpty()) {
 			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getAlleleVariantSOIds());
 			domain.setAlleleVariantSOIds(IteratorUtils.toList(acc.iterator()));
 		}
 		
 		if (entity.getMpIds() != null && !entity.getMpIds().isEmpty()) {
 			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMpIds());
 			domain.setMpIds(IteratorUtils.toList(acc.iterator()));
 		}
 		
 		if (entity.getDoIds() != null && !entity.getDoIds().isEmpty()) {
 			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getDoIds());
 			domain.setDoIds(IteratorUtils.toList(acc.iterator()));
 		}
 	
		return domain;
	}

}

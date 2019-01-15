package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;

public class AnnotationTranslator extends BaseEntityDomainTranslator<Annotation, AnnotationDomain> {
	
	private EvidenceTranslator evidenceTranslator = new EvidenceTranslator();
	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected AnnotationDomain entityToDomain(Annotation entity, int translationDepth) {
		AnnotationDomain domain = new AnnotationDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));
		domain.setAnnotTypeKey(String.valueOf(entity.getAnnotType().get_annotType_key()));
		domain.setAnnotType(entity.getAnnotType().getName());
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		domain.setQualifierKey(String.valueOf(entity.getQualifier().get_term_key()));
		domain.setQualifier(entity.getQualifier().getTerm());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// annotation has one evidence but is represented in OneToMany in entity
		// some annotation types (_annottype_key in (1008, 1009, 1014)
		// do not have a evidence record
		if (entity.getEvidences() != null) {
			Iterable<EvidenceDomain> i = evidenceTranslator.translateEntities(entity.getEvidences());
			if(i.iterator().hasNext() == true) {
				domain.setEvidence(i.iterator().next());			
			}
		}

		if (entity.getMarkerFeatureTypeIds() != null) {
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
			if(acc.iterator().hasNext() == true) {
				domain.setMarkerFeatureTypeIds(IteratorUtils.toList(acc.iterator()));
			}
		}	
		
		return domain;
	}

	@Override
	protected Annotation domainToEntity(AnnotationDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

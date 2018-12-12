package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;

public class AnnotationTranslator extends BaseEntityDomainTranslator<Annotation, AnnotationDomain> {
	
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

		// one-to-many primary accession ids
		if (entity.getMarkerFeatureTypeIds() != null) {
			AccessionTranslator accessionTranslator = new AccessionTranslator();
			Iterable<AccessionDomain> i = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
			if(i.iterator().hasNext() == true) {
				domain.setMarkerFeatureTypeIds(IteratorUtils.toList(i.iterator()));
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

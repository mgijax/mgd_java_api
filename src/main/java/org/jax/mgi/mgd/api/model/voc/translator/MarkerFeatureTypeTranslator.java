package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

public class MarkerFeatureTypeTranslator extends BaseEntityDomainTranslator<Annotation, MarkerFeatureTypeDomain> {

	SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected MarkerFeatureTypeDomain entityToDomain(Annotation entity, int translationDepth) {
		MarkerFeatureTypeDomain domain = new MarkerFeatureTypeDomain();

		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		domain.setMarkerFeatureTypeId(accessionTranslator.translate(entity.getMarkerFeatureTypeId()));
				
		return domain;
	}

	@Override
	protected Annotation domainToEntity(MarkerFeatureTypeDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

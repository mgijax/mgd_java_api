package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

public class MarkerFeatureTypeTranslator extends BaseEntityDomainTranslator<Annotation, MarkerFeatureTypeDomain> {

	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();

	@Override
	protected MarkerFeatureTypeDomain entityToDomain(Annotation entity, int translationDepth) {
		MarkerFeatureTypeDomain domain = new MarkerFeatureTypeDomain();

		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());

		// one-to-many primary accession ids
		if (entity.getMarkerFeatureTypeIds() != null) {
			Iterable<SlimAccessionDomain> i = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
			if(i.iterator().hasNext() == true) {
				domain.setMarkerFeatureTypeIds(IteratorUtils.toList(i.iterator()));
			}
		}
				
		return domain;
	}

	@Override
	protected Annotation domainToEntity(MarkerFeatureTypeDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

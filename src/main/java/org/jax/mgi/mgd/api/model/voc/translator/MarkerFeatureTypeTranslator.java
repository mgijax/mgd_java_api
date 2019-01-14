package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;

public class MarkerFeatureTypeTranslator extends BaseEntityDomainTranslator<Annotation, MarkerFeatureTypeDomain> {

	SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected MarkerFeatureTypeDomain entityToDomain(Annotation entity, int translationDepth) {
		MarkerFeatureTypeDomain domain = new MarkerFeatureTypeDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));		
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		
		Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
		if(acc.iterator().hasNext() == true) {
			domain.setMarkerFeatureTypeIds(IteratorUtils.toList(acc.iterator()));
		}
				
		return domain;
	}

	@Override
	protected Annotation domainToEntity(MarkerFeatureTypeDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}

package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;

public class MarkerFeatureTypeTranslator extends BaseEntityDomainTranslator<Annotation, MarkerFeatureTypeDomain> {
	
	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	//private EvidenceTranslator evidenceTranslator = new EvidenceTranslator();
	
	@Override
	protected MarkerFeatureTypeDomain entityToDomain(Annotation entity) {
		MarkerFeatureTypeDomain domain = new MarkerFeatureTypeDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));	
		domain.setAnnotTypeKey(String.valueOf(entity.getAnnotType().get_annotType_key()));
		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
		domain.setTerm(entity.getTerm().getTerm());
		
		if (entity.getMarkerFeatureTypeIds() != null && !entity.getMarkerFeatureTypeIds().isEmpty()) {
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMarkerFeatureTypeIds());
			domain.setMarkerFeatureTypeIds(IteratorUtils.toList(acc.iterator()));
		}
				
		return domain;
	}

}

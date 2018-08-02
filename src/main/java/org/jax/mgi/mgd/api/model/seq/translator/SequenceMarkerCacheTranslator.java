package org.jax.mgi.mgd.api.model.seq.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceMarkerCacheDomain;
import org.jax.mgi.mgd.api.model.seq.entities.SequenceMarkerCache;

public class SequenceMarkerCacheTranslator extends BaseEntityDomainTranslator<SequenceMarkerCache, SequenceMarkerCacheDomain> {

	@Override
	protected SequenceMarkerCacheDomain entityToDomain(SequenceMarkerCache entity, int translationDepth) {
		
		SequenceMarkerCacheDomain domain = new SequenceMarkerCacheDomain();
		domain.set_cache_key(entity.get_cache_key());
		domain.setLogicalDB(entity.getLogicalDB().getName());
		domain.setAccid(entity.getAccID());
		domain.setRawbiotype(entity.getRawbiotype());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected SequenceMarkerCache domainToEntity(SequenceMarkerCacheDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

package org.jax.mgi.mgd.api.model.seq.translator;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.seq.domain.SeqMarkerCacheDomain;
import org.jax.mgi.mgd.api.model.seq.entities.SeqMarkerCache;

public class SeqMarkerCacheTranslator extends BaseEntityDomainTranslator<SeqMarkerCache, SeqMarkerCacheDomain> {

	@Override
	protected SeqMarkerCacheDomain entityToDomain(SeqMarkerCache entity, int translationDepth) {
		SeqMarkerCacheDomain domain = new SeqMarkerCacheDomain();
		domain.setCacheKey(entity.get_cache_key());
		domain.setAccID(entity.getAccID());
		domain.setRawbiotype(entity.getRawbiotype());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setAnnotation_date(entity.getAnnotation_date());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());

		if(translationDepth > 0) {
		}
		
		return domain;
	}

	@Override
	protected SeqMarkerCache domainToEntity(SeqMarkerCacheDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}

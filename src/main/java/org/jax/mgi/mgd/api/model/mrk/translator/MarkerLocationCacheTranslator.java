package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerLocationCacheDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerLocationCache;

public class MarkerLocationCacheTranslator extends BaseEntityDomainTranslator<MarkerLocationCache, MarkerLocationCacheDomain> {

	@Override
	protected MarkerLocationCacheDomain entityToDomain(MarkerLocationCache entity) {

		MarkerLocationCacheDomain domain = new MarkerLocationCacheDomain();

		domain.set_marker_key(entity.get_marker_key());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setCmOffset(entity.getCmOffset());
		domain.setGenomicChromosome(entity.getGenomicChromosome());
		domain.setStartCoordinate(entity.getStartCoordinate());
		domain.setEndCoordinate(entity.getEndCoordinate());
		domain.setStrand(entity.getStrand());
		domain.setMapUnits(entity.getMapUnits());
		domain.setProvider(entity.getProvider());
		domain.setVersion(entity.getVersion());
		
		return domain;
	}

}

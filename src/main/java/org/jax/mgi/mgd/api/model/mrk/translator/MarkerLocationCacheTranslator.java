package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerLocationCacheDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerLocationCache;
import org.jboss.logging.Logger;

public class MarkerLocationCacheTranslator extends BaseEntityDomainTranslator<MarkerLocationCache, MarkerLocationCacheDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected MarkerLocationCacheDomain entityToDomain(MarkerLocationCache entity) {
		
		MarkerLocationCacheDomain domain = new MarkerLocationCacheDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setChromosome(entity.getChromosome());
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setOrganismLatin(entity.getOrganism().getLatinname());
		domain.setMarkerTypeKey(entity.getMarkerType().get_marker_type_key().toString());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		
		if (entity.getCytogeneticOffset() != null) {
			domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		}
		
		if (entity.getCmOffset() != null) {
			domain.setCmOffset(entity.getCmOffset().toString());
		}
		
		if (entity.getGenomicChromosome() != null) {
			domain.setGenomicChrommosome(entity.getGenomicChromosome());
		}
		
		if (entity.getStartCoordinate() != null) {
			domain.setStartCoordinate(String.valueOf(entity.getStartCoordinate()));
			domain.setEndCoordinate(String.valueOf(entity.getEndCoordinate()));
		}
		
		if (entity.getMapUnits() != null) {
			domain.setMapUnits(entity.getMapUnits());
		}
		
		if (entity.getProvider() != null) {
			domain.setProvider(entity.getProvider());
		}
		
		if (entity.getVersion() != null) {
			domain.setVersion(entity.getVersion());;
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));					
		
		return domain;
	}

}

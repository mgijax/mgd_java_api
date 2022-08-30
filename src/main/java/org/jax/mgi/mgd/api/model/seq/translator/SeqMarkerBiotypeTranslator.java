package org.jax.mgi.mgd.api.model.seq.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.seq.domain.SeqMarkerBiotypeDomain;
import org.jax.mgi.mgd.api.model.seq.entities.SeqMarkerCache;
import org.jboss.logging.Logger;

public class SeqMarkerBiotypeTranslator extends BaseEntityDomainTranslator<SeqMarkerCache, SeqMarkerBiotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected SeqMarkerBiotypeDomain entityToDomain(SeqMarkerCache entity) {
		
		SeqMarkerBiotypeDomain domain = new SeqMarkerBiotypeDomain();
		
		domain.setCacheKey(String.valueOf(entity.get_cache_key()));	
		domain.setBiotypeConflictKey(String.valueOf(entity.get_biotypeConflict_key()));
		domain.setAccID(entity.getAccID());
		domain.setRawbiotype(entity.getRawbiotype());
		
		if (entity.get_biotypeConflict_key() == 5420767) {
			domain.setIsBiotypeConflict(true);
		}
		else {
			domain.setIsBiotypeConflict(false);
		}
		
		return domain;
	}

}

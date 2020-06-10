package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jboss.logging.Logger;

public class SlimMarkerTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimMarkerDomain entityToDomain(Marker entity) {
			
		SlimMarkerDomain domain = new SlimMarkerDomain();
		
		log.info("marker key");
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		log.info("symbol");
		domain.setSymbol(entity.getSymbol());
		log.info("name");
		domain.setName(entity.getName());
		log.info("chr");
		domain.setChromosome(entity.getChromosome());
		log.info("organism key");
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		log.info("organism");
		domain.setOrganism(entity.getOrganism().getCommonname());
		log.info("latin");
		domain.setOrganismLatin(entity.getOrganism().getLatinname());
		log.info("modified by key");
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		log.info("modified by");
		domain.setModifiedBy(entity.getModifiedBy().getLogin());

//		if (entity.getMgiAccessionIds() != null) {
//			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
//		}
		
		return domain;
	}

}

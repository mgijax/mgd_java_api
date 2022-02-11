package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jboss.logging.Logger;

public class AlleleFearTranslator extends BaseEntityDomainTranslator<Allele, AlleleFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected AlleleFearDomain entityToDomain(Allele entity) {
		
		AlleleFearDomain domain = new AlleleFearDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setAlleleDisplay(entity.getSymbol() + ", " + entity.getName());
		domain.setSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// relationship domain by allele
		// TO-DO/add List<RelationshipFear> to entity/Allele
//		if (entity.getRelationships() != null && !entity.getRelationships().isEmpty()) {
//			RelationshipFearTranslator fearTranslator = new RelationshipFearTranslator();	
//			Iterable<RelationshipFearDomain> t = fearTranslator.translateEntities(entity.getRelationships());			
//			domain.setRelationships(IteratorUtils.toList(t.iterator()));
//			domain.getRelationships().sort(Comparator.comparing(RelationshipFearDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
//		}
		
		return domain;
	}

}

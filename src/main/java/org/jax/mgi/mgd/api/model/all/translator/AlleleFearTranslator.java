package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByAlleleDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipFearByAlleleTranslator;
import org.jboss.logging.Logger;

public class AlleleFearTranslator extends BaseEntityDomainTranslator<Allele, AlleleFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected AlleleFearDomain entityToDomain(Allele entity) {
		
		AlleleFearDomain domain = new AlleleFearDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setAlleleSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// relationship domain by allele/mutation_involves
		if (entity.getMutationInvolves() != null && !entity.getMutationInvolves().isEmpty()) {
			RelationshipFearByAlleleTranslator fearTranslator = new RelationshipFearByAlleleTranslator();	
			Iterable<RelationshipFearByAlleleDomain> t = fearTranslator.translateEntities(entity.getMutationInvolves());			
			domain.setMutationInvolves(IteratorUtils.toList(t.iterator()));
			domain.getMutationInvolves().sort(Comparator.comparing(RelationshipFearByAlleleDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
		}
		
		// relationship domain by allele/expresses_component
		if (entity.getExpressesComponents() != null && !entity.getExpressesComponents().isEmpty()) {
			RelationshipFearByAlleleTranslator fearTranslator = new RelationshipFearByAlleleTranslator();	
			Iterable<RelationshipFearByAlleleDomain> t = fearTranslator.translateEntities(entity.getExpressesComponents());			
			domain.setExpressesComponents(IteratorUtils.toList(t.iterator()));
			domain.getExpressesComponents().sort(Comparator.comparing(RelationshipFearByAlleleDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
		}
		
		// relationship domain by allele/driver_component
		if (entity.getDriverComponents() != null && !entity.getDriverComponents().isEmpty()) {
			RelationshipFearByAlleleTranslator fearTranslator = new RelationshipFearByAlleleTranslator();	
			Iterable<RelationshipFearByAlleleDomain> t = fearTranslator.translateEntities(entity.getDriverComponents());
			domain.setDriverComponents(IteratorUtils.toList(t.iterator()));
			domain.getDriverComponents().sort(Comparator.comparing(RelationshipFearByAlleleDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		
		return domain;
	}

}

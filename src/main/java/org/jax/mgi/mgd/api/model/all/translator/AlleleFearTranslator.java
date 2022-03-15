package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipFearTranslator;
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
			RelationshipFearTranslator fearTranslator = new RelationshipFearTranslator();	
			Iterable<RelationshipFearDomain> t = fearTranslator.translateEntities(entity.getMutationInvolves());			
			domain.setMutationInvolves(IteratorUtils.toList(t.iterator()));
			domain.getMutationInvolves().sort(Comparator.comparing(RelationshipFearDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
		}
		
		// relationship domain by allele/expresses_component
		if (entity.getMutationInvolves() != null && !entity.getExpressesComponents().isEmpty()) {
			RelationshipFearTranslator fearTranslator = new RelationshipFearTranslator();	
			Iterable<RelationshipFearDomain> t = fearTranslator.translateEntities(entity.getExpressesComponents());			
			domain.setExpressesComponents(IteratorUtils.toList(t.iterator()));
			domain.getExpressesComponents().sort(Comparator.comparing(RelationshipFearDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
		}
		
		return domain;
	}

}

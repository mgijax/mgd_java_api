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
		if (entity.getExpressesComponents() != null && !entity.getExpressesComponents().isEmpty()) {
			RelationshipFearTranslator fearTranslator = new RelationshipFearTranslator();	
			Iterable<RelationshipFearDomain> t = fearTranslator.translateEntities(entity.getExpressesComponents());			
			domain.setExpressesComponents(IteratorUtils.toList(t.iterator()));
			domain.getExpressesComponents().sort(Comparator.comparing(RelationshipFearDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
		}
		
		// relationship domain by allele/driver_component for non-recombinase
		if (entity.getDriverComponents() != null && !entity.getDriverComponents().isEmpty()) {
			// check if allele is 'Recombinase'
			Boolean isRecombinase = false;
//			if (entity.getSubtypeAnnots() != null && !entity.getSubtypeAnnots().isEmpty()) {
//				for(int i = 0; i < entity.getSubtypeAnnots().size(); i++) {
//					if (entity.getSubtypeAnnots().get(i).getTerm().get_term_key() == 11025588) {
//						isRecombinase = true;
//					}
//				}
//			}
			// if allele is not 'Recombinase', then load any driver components that exist
			if (isRecombinase == false) {
				RelationshipFearTranslator fearTranslator = new RelationshipFearTranslator();	
				Iterable<RelationshipFearDomain> t = fearTranslator.translateEntities(entity.getDriverComponents());
				domain.setDriverComponents(IteratorUtils.toList(t.iterator()));
				domain.getDriverComponents().sort(Comparator.comparing(RelationshipFearDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));
			}
		}
		
		return domain;
	}

}

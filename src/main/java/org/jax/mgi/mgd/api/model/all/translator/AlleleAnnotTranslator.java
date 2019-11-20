package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.domain.AlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class AlleleAnnotTranslator extends BaseEntityDomainTranslator<Allele, AlleleAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private AnnotationTranslator annotTranslator = new AnnotationTranslator();	
	
	@Override
	protected AlleleAnnotDomain entityToDomain(Allele entity) {
		
		AlleleAnnotDomain domain = new AlleleAnnotDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		log.info("done setting alleleKey in domain: " + entity.get_allele_key());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		// do annotations by allele
		if (entity.getDoAnnots() != null && !entity.getDoAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getDoAnnots());			
			domain.setAnnots(IteratorUtils.toList(t.iterator()));
			domain.getAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));	
		}
	
		// Note: DO annotations have no header
		
		return domain;
	}

}

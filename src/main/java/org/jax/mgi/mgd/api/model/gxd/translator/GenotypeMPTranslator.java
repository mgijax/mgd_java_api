package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationHeaderTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class GenotypeMPTranslator extends BaseEntityDomainTranslator<Genotype, GenotypeMPDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();	
	private AnnotationTranslator annotTranslator = new AnnotationTranslator();
	private AnnotationHeaderTranslator annotHeaderTranslator = new AnnotationHeaderTranslator();
	
	@Override
	protected GenotypeMPDomain entityToDomain(Genotype entity) {
		
		GenotypeMPDomain domain = new GenotypeMPDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
			
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
	
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		// mp annotations by genotype
		if (entity.getMpAnnots() != null && !entity.getMpAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getMpAnnots());
			domain.setMpAnnots(IteratorUtils.toList(t.iterator()));
		}
		
		// mp header by genotype
		if (entity.getMpHeaders() != null && !entity.getMpHeaders().isEmpty()) {
			Iterable<AnnotationHeaderDomain> t = annotHeaderTranslator.translateEntities(entity.getMpHeaders());
			domain.setMpHeaders(IteratorUtils.toList(t.iterator()));
		}
							
		return domain;
	}

}

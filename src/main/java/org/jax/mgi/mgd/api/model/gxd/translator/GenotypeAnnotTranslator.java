package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeAnnotDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeAnnotHeaderViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationHeaderTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class GenotypeAnnotTranslator extends BaseEntityDomainTranslator<Genotype, GenotypeAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	// sc 9/18 updated this from and GenotypeMPAnnotationTranslator as we changed the GenotypeMPDomain to
	// have a list of AnnotationDomains instead of GenotypeMPAnnotationDomains
	private AnnotationTranslator annotTranslator = new AnnotationTranslator();
	private AnnotationHeaderTranslator annotHeaderTranslator = new AnnotationHeaderTranslator();
	
	@Override
	protected GenotypeAnnotDomain entityToDomain(Genotype entity) {
		
		GenotypeAnnotDomain domain = new GenotypeAnnotDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
		//log.info("done setting genotypeKey in domain: " + entity.get_genotype_key());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// We have both MP and DO. Create a single list from both to set in domain
		List<AnnotationDomain> newList  = new ArrayList<AnnotationDomain>();
		
		// do annotations by genotype
		if (entity.getDoAnnots() != null && !entity.getDoAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getDoAnnots());			
			newList.addAll(IteratorUtils.toList(t.iterator()));
		}
		
		// mp annotations by genotype
		if (entity.getMpAnnots() != null && !entity.getMpAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getMpAnnots());
			newList.addAll(IteratorUtils.toList(t.iterator()));		
		}
		
		// now set the joined list in the domain
		domain.setAnnots(newList);
		
		// now order the annotations
		domain.getAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));	
				
		// only mp has headers - mp header by genotype
		if (entity.getMpHeaders() != null && !entity.getMpHeaders().isEmpty()) {
			Iterable<AnnotationHeaderDomain> t2 = annotHeaderTranslator.translateEntities(entity.getMpHeaders());
			domain.setHeaders(IteratorUtils.toList(t2.iterator()));
			domain.getHeaders().sort(Comparator.comparingInt(AnnotationHeaderDomain::getSequenceNum));
		}
		
		// mp headerByAnnot by annotations
//		if (entity.getMpHeadersByAnnot() != null && !entity.getMpHeadersByAnnot().isEmpty()) {
//			GenotypeAnnotHeaderViewTranslator genotypeHeaderTransltor = new GenotypeAnnotHeaderViewTranslator();
//			Iterable<GenotypeAnnotHeaderViewDomain> t3 = genotypeHeaderTransltor.translateEntities(entity.getMpHeadersByAnnot());
//			domain.setHeadersByAnnot(IteratorUtils.toList(t3.iterator()));
//		}
		
		// Note: do annotations have no header
		
		//log.info("count of annotations: " + domain.getAnnots().size());
		return domain;
	}

}

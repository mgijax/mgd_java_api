package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleVariantAnnotationTranslator extends BaseEntityDomainTranslator<Annotation, AlleleVariantAnnotationDomain> {

	SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
    @Override
    protected AlleleVariantAnnotationDomain entityToDomain(Annotation entity, int translationDepth) {
    	AlleleVariantAnnotationDomain domain = new AlleleVariantAnnotationDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));		    	
 		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
 		domain.setTerm(entity.getTerm().getTerm());

 		if (entity.getAlleleVariantSOIds() != null) {
 			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getAlleleVariantSOIds());
 			if(acc.iterator().hasNext() == true) {
 				domain.setAlleleVariantSOIds(IteratorUtils.toList(acc.iterator()));
 			}
 		}
 		
 		return domain;
    }

    @Override
    protected Annotation domainToEntity(AlleleVariantAnnotationDomain domain, int translationDepth) {
        // Needs to be implemented once we choose to save terms
        return null;
    }

}

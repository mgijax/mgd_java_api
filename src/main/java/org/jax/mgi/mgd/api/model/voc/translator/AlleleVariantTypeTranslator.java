package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

public class AlleleVariantTypeTranslator extends BaseEntityDomainTranslator<Annotation, AlleleVariantTypeDomain> {

	SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
    @Override
    protected AlleleVariantTypeDomain entityToDomain(Annotation entity, int translationDepth) {
     	AlleleVariantTypeDomain domain = new AlleleVariantTypeDomain();

     	domain.setAnnotKey(String.valueOf(entity.get_annot_key()));
 		domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
 		domain.setTerm(entity.getTerm().getTerm());
		//domain.setAlleleVariantTypeId(accessionTranslator.translate(entity.getAlleleVariantSOId()));
 		
 		if (entity.getTerm().getAccessionId() != null) {
 			domain.setAccID(entity.getTerm().getAccessionId().getAccID());
 		}
 		return domain;
    }

    @Override
    protected Annotation domainToEntity(AlleleVariantTypeDomain domain, int translationDepth) {
        // Needs to be implemented once we choose to save terms
        return null;
    }

}

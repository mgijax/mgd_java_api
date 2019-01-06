package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

public class AlleleVariantTypeTranslator extends BaseEntityDomainTranslator<Annotation, AlleleVariantTypeDomain> {

         private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();

     @Override
     protected AlleleVariantTypeDomain entityToDomain(Annotation entity, int translationDepth) {
             AlleleVariantTypeDomain domain = new AlleleVariantTypeDomain();

             domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
             domain.setTerm(entity.getTerm().getTerm());

             // one-to-many variant types
             if (entity.getAlleleVariantSOIds() != null) {
                     Iterable<SlimAccessionDomain> i = accessionTranslator.translateEntities(entity.getAlleleVariantSOIds());
                     if(i.iterator().hasNext() == true) {
                             domain.setAlleleVariantTypeIds(IteratorUtils.toList(i.iterator()));
                     }
             }

             return domain;
     }

     @Override
     protected Annotation domainToEntity(AlleleVariantTypeDomain domain, int translationDepth) {
             // Needs to be implemented once we choose to save terms
             return null;
     }

}


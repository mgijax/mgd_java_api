package org.jax.mgi.mgd.api.model.voc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantEffectDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

public class AlleleVariantEffectTranslator extends BaseEntityDomainTranslator<Annotation, AlleleVariantEffectDomain> {

         private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();

     @Override
     protected AlleleVariantEffectDomain entityToDomain(Annotation entity, int translationDepth) {
             AlleleVariantEffectDomain domain = new AlleleVariantEffectDomain();

             domain.setTermKey(String.valueOf(entity.getTerm().get_term_key()));
             domain.setTerm(entity.getTerm().getTerm());

             
             // one-to-many variant effects
             if (entity.getAlleleVariantSOIds() != null) {
                     Iterable<SlimAccessionDomain> i = accessionTranslator.translateEntities(entity.getAlleleVariantSOIds());
                     if(i.iterator().hasNext() == true) {
                             domain.setAlleleVariantEffectIds(IteratorUtils.toList(i.iterator()));
                     }
             }

             return domain;
     }

     @Override
     protected Annotation domainToEntity(AlleleVariantEffectDomain domain, int translationDepth) {
             // Needs to be implemented once we choose to save terms
             return null;
     }

}


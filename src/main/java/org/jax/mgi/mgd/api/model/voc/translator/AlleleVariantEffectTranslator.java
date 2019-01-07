package org.jax.mgi.mgd.api.model.voc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
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
		//domain.setAlleleVariantEffectId(accessionTranslator.translate(entity.getAlleleVariantSOId()));
 				
  		return domain;
     }

    @Override
    protected Annotation domainToEntity(AlleleVariantEffectDomain domain, int translationDepth) {
        // Needs to be implemented once we choose to save terms
        return null;
    }

}


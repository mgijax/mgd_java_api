package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Allele Variant Type Domain")
public class AlleleVariantEffectDomain extends BaseDomain {

    private String termKey;
    private String term;
 	private List<SlimAccessionDomain> alleleVariantSOIds;

}

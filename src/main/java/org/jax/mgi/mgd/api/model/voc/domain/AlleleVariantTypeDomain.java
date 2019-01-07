package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Allele Variant Type Domain")
public class AlleleVariantTypeDomain extends BaseDomain {

	private String annotKey;
    private String termKey;
    private String term;
    private String accID;
    //private SlimAccessionDomain alleleVariantTypeId;
}

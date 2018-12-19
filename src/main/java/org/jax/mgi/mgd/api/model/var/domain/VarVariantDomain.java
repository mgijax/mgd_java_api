package org.jax.mgi.mgd.api.model.var.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarVariantDomain extends BaseDomain {
	
	private String variantKey;
	private String isReviewed;
	private String description;
	private String alleleKey;
	private String alleleSymbol;
	private String sourceVariantKey;	
	private String strainKey;
	private String strain;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;   	
}

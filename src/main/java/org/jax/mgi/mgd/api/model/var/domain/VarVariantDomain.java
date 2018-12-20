package org.jax.mgi.mgd.api.model.var.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarVariantDomain extends BaseDomain {
	
	private String variantKey;
	private String isReviewed;
	private String description;
	private String sourceVariantKey;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private SlimAlleleDomain allele;
	private SlimProbeStrainDomain strain;
	
} 

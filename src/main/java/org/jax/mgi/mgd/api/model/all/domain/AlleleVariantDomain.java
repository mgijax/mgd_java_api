package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleVariantDomain extends BaseDomain {
	
	private String variantKey;
	private String isReviewed;
	private String description;
	private String sourceVariantKey;
	private String chromosome;
	private String strand;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private SlimAlleleVariantDomain allele;
	private SlimProbeStrainDomain strain;
	
	private String processStatus;
} 

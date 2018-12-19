package org.jax.mgi.mgd.api.model.var.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarVariantDomain extends BaseDomain {
	
	private String variantKey;
	private String isReviewed;
	private String description;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;   
	
	List<AlleleDomain> allele;
	List<VarVariantDomain> sourceVariant;
	List<ProbeStrainDomain> strain;
	
}

package org.jax.mgi.mgd.api.model.var.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarTypeDomain extends BaseDomain {
	
	private String varTypeKey;
	private String variantKey;
	private String typeKey;
	private String typeTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;    
}

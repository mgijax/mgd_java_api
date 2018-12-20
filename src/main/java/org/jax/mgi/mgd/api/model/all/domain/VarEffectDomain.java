package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarEffectDomain extends BaseDomain {
	
	private String.allEffectKey;
	private String.alliantKey;
	private String.alliantTerm;
	private String effectKey;
	private String effectTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;    
}

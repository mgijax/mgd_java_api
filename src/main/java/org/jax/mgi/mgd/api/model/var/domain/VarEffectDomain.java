package org.jax.mgi.mgd.api.model.var.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarEffectDomain extends BaseDomain {
	
	private String varEffectKey;
	private String creation_date;
	private String modification_date;     
}

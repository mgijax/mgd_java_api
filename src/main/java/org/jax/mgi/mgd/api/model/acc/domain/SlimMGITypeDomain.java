package org.jax.mgi.mgd.api.model.acc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMGITypeDomain extends BaseDomain {

	private String mgiTypeKey;	
	private String name;
}

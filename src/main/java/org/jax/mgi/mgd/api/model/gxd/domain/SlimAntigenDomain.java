package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAntigenDomain extends BaseDomain {

	// a slim version of AntigenDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String antigenKey;
	private String antigenName;
}

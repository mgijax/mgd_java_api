package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAntibodyDomain extends BaseDomain {

	// a slim version of AntibodyDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String antibodyKey;
	private String antibodyName;
	private String accID;
}

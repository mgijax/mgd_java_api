package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimUserDomain extends BaseDomain {

	// a slim version of UserDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String userKey;
	private String userLogin;
}

package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimImageDomain extends BaseDomain {

	// a slim version of ImageDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String imageKey;
	private String imageDisplay;
}

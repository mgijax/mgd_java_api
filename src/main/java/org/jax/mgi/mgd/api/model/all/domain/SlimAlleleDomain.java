package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleDomain extends BaseDomain {

	// a slim version of AlleleDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String alleleKey;
	private String symbol;
	private SlimMGIReferenceAssocDomain references;
	
}

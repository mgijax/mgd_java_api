package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleVariantDomain extends BaseDomain {

	// a slim version of AlleleVariantDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String variantKey;
	private String alleleKey;
	private String symbol;
	private String strainKey;
	private String strain;
	
}

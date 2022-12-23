package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAllelePairDomain extends BaseDomain {

	private String allelePairKey;
	private String genotypeKey;
	private String isConditional;	
	private String alleleKey1;
	private String alleleSymbol1;
	private String alleleKey2;
	private String alleleSymbol2;

	
}

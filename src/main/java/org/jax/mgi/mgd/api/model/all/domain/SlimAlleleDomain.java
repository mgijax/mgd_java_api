package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;
	private String markerKey;
	private String markerSymbol;
	private String accID;
}

package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleFearDomain extends BaseDomain {

	private String alleleKey;
	private String alleleDisplay;
	private String alleleSymbol;
	private String accID;
}

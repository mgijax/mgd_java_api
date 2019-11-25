package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleAnnotDomain extends BaseDomain {

	private String alleleKey;
	private String alleleDisplay;
	private String accID;

}

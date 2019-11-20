package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimGenotypeDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private String accID;
	private String resourceIdentifierId;
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeAnnotHeaderViewDomain extends BaseDomain {

	private String headerTermKey;
	private String headerTerm;
	private String termKey;
	private String term;
}

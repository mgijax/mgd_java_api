package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimGenotypeReferenceDomain extends BaseDomain {
	// used by GenotypeMPService/validateAlleleReference

	private String genotypeKey;
	private String refsKey;
}

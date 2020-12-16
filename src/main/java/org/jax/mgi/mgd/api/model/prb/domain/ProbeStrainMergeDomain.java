package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeStrainMergeDomain extends BaseDomain {

	private String incorrectStrainKey;
	private String incorrectStrain;
	private String correctStrainKey;
	private String correctStrain;	
}

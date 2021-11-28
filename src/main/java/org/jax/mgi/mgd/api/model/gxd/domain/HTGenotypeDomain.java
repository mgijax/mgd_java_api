package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTGenotypeDomain extends BaseDomain {
	
	private int _genotype_key;
	private int _strain_key;
	private Integer isConditional;
	private String mgiid;
	private String combination1_cache;
	private String geneticbackground;
}

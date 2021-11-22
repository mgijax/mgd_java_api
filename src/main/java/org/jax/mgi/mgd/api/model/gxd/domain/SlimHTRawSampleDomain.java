package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimHTRawSampleDomain extends BaseDomain {
	
	private HTRawSampleDomain raw_sample;	
	private String name;
}

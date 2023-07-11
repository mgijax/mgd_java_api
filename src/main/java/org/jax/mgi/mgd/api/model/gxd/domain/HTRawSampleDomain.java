package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTRawSampleDomain extends BaseDomain {
	
	private Integer _rawsample_key;
	private Integer _experiment_key;
	private String accid;
	private List<HTRawSampleVariableDomain> variable;

}

package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimProbeDomain extends BaseDomain {

	private String processStatus;
	private String probeKey;
	private String name;
	private String accID;

}

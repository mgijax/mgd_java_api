package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimProbeSummaryDomain extends BaseDomain {

	private String probeKey;
	private String name;
	private String accID;

}

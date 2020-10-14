package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeAccRefDomain extends BaseDomain {

	private String referenceKey;
	private String probeKey;	
	private String accessionKey;
	private String logicaldbKey;
	private String logicaldbName;
	private String accID;
}

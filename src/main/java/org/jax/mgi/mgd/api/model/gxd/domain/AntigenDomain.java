package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntigenDomain extends BaseDomain {

	private String antigenKey;
	private String antigenName;
	private String regionCovered;
	private String antigenNote;		
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;

	// probe-source info
	private ProbeSourceDomain probeSource;	
}

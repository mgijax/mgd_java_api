package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyPrepDomain extends BaseDomain {

	private String processStatus;
	private String antibodyPrepKey;
	private String antibodyKey;
	private String antibodyName;
	private String antibodyAccID;
	private String secondaryKey;
	private String secondaryName;
	private String labelKey;
	private String labelName;
	private String creation_date;
	private String modification_date;

}

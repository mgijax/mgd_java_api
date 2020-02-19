package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbePrepDomain extends BaseDomain {

	private String processStatus;
	private String probePrepKey;
	private String probeKey;
	private String probeName;
	private String probeAccID;
	private String probeSenseKey;
	private String probeSenseName;
	private String labelKey;
	private String labelName;
	private String visualizationMethodKey;
	private String visualiationMethod;
	private String prepType;
	private String creation_date;
	private String modification_date;

}

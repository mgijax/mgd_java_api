package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerDomain extends BaseDomain {

	private Integer _marker_key;
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String organism;
	private String markerStatus;
	private String markerType;
	private String createdBy;
	private String modifiedBy;
	private String mgiAccessionId;

}

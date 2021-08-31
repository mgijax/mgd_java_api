package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GelRowDomain extends BaseDomain {

	private String processStatus;
	private String gelRowKey;
	private String assayKey;
	private String gelUnitsKey;
	private String gelUnits;
	private Integer sequenceNum;
	private String size;
	private String rowNote;
	private String creation_date;
	private String modification_date;
}

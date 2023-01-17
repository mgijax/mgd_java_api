package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryGXDIndexDomain extends BaseDomain {

	private String indexKey;
	private String markerKey;
	private String markerID;
	private String markerSymbol;
	private String markerName;
	private String markerStatus;
	private String markerType;
	private String indexAssayKey;
	private String indexAssay;
	private String sequenceNum;
	private String age;
	private String priority;
	private String conditional;
	private String notes;
	private String isFullCoded;
	private String jnumID;
	private String shortCitation;

}

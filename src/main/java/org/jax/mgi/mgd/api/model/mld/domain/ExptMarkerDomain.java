package org.jax.mgi.mgd.api.model.mld.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExptMarkerDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String exptKey;
	private String markerKey;
	private String markerSymbol;
	private String markerAccID;
	private String alleleKey;
	private String alleleSymbol;
	private String assayTypeKey;
	private String assayType;
	private Integer sequenceNum;
	private String gene;
	private String description;
	//private Integer matrixData;
	private String creation_date;
	private String modification_date;

}

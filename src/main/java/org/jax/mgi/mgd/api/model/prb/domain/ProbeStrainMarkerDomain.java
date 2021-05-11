package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeStrainMarkerDomain extends BaseDomain {

	private String processStatus;
	private String strainMarkerKey;
	private String strainKey;
	private String markerKey;
	private String markerSymbol;
	private String chromosome;
    private String markerAccID;	
	private String alleleKey;
	private String alleleSymbol;
	private String strainOfOrigin;
	private String qualifierKey;
	private String qualifierTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;

}

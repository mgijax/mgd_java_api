package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMarkerDomain extends BaseDomain {

	// a slim version of MarkerDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String markerKey;
	private String symbol;
	private String name;
    private String chromosome;
    private String accID;
	private String organismKey;
	private String organism;
	private String organismLatin;
	private String markerStatusKey;	
	private String markerStatus;
	private String markerTypeKey;
	private String markerType;
	private String modifiedByKey;
	private String modifiedBy;
	private MarkerNoteDomain detailClip;
	
}

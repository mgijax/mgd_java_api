package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleFearRegionDomain extends BaseDomain {
	
	// used by service/allelefear/getMarkerByRegion()
	
	private String markerKey;
	private String alleleKey;
	private String symbol;
	private String name;
    private String chromosome;
    private String accID;
	private String startCoordinate;
	private String endCoordinate;	
	private String relationshipTermKey;
}

package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMarkerFearDomain extends BaseDomain {

	// used by service/markerfear/search()
	
	private String markerKey;
	private String markerDisplay;
	private String markerSymbol;
	private String accID;
}

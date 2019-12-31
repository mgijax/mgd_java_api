package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMarkerAnnotDomain extends BaseDomain {

	private String markerKey;
	private String markerDisplay;
	private String accID;

}

package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerTypeDomain extends BaseDomain {
	
	private String markerTypeKey;
	private String markerType;
	private String creation_date;
	private String modification_date;     
}

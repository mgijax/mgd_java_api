package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerNoteDomain extends BaseDomain {
	
	private String markerKey;
	private String note;
//	private String creation_date;
//	private String modification_date;     
}

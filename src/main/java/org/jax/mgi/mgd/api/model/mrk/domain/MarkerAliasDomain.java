package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerAliasDomain extends BaseDomain {
	
	//private String processStatus;
	private String aliasKey;
	private String markerPrimary;
	private String markerKey;
	private String markerAlias;
	private String creation_date;
	private String modification_date;    
}

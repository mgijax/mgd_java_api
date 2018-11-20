package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventDomain extends BaseDomain {
	
	private Integer markerEventKey;
	private String event;
	private String creation_date;
	private String modification_date;     
}

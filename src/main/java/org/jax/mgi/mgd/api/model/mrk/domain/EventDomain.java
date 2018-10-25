package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventDomain extends BaseDomain {
	
	private Integer markerEventKey;
	private String event;
	private Date creation_date;
	private Date modification_date;     
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GelBandDomain extends BaseDomain {

	private String processStatus;
	private String gelBandKey;
	private String gelLaneKey;
	private String strengthKey;
	private String strength;
	private String bandNote;
	private String gelRowKey;
	private String assayKey;	
	private Integer sequenceNum;			
	private String creation_date;
	private String modification_date;
	
}

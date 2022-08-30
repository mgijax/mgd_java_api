package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerLocationCacheDomain extends BaseDomain {
	
	private String markerKey;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
	private String organismKey;
	private String organism;
	private String organismLatin;
	private String markerTypeKey;
	private String markerType;
	private String sequenceNum;
	private String genomicChrommosome;
	private String startCoordinate;
	private String endCoordinate;
	private String mapUnits;
	private String provider;
	private String version;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;

}

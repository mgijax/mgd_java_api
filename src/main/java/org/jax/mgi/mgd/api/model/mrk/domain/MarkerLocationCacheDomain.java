package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerLocationCacheDomain extends BaseDomain {
	
	private Integer _marker_key;
	private String chromosome;
	private String cytogeneticOffset;
	private Integer cmOffset;
	private String genomicChromosome;
	private Integer startCoordinate;
	private Integer endCoordinate;
	private String strand;
	private String mapUnits;
	private String provider;
	private String version;
	
}

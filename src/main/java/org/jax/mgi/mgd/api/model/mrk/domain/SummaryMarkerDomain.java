package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryMarkerDomain extends BaseDomain {

	private String jnumID;
	private String markerKey;
	private String symbol;
	private String name;
    private String accID;
	private String markerStatus;
	private String markerType;
	private String featureTypes;
	private String synonyms;	
}

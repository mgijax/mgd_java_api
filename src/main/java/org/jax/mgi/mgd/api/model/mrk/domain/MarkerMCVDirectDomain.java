package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerMCVDirectDomain extends BaseDomain {
	
	private String markerKey;
	private String mcvTermKey;
	private String term;
	private String qualifier;
	private String directTerms;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;    
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimEmapaDomain extends BaseDomain {

	// a slim version of AssayDomain, SpecimenDomain, EMAPA members
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String assayKey;
	private String specimenKey;
	private String gelLaneKey;
	private String createdByKey;
	private String createdBy;
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimCellTypeDomain extends BaseDomain {

	// a slim version of AssayDomain, SpecimenDomain, GelLaneDomain, Cell Type members
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String assayKey;
	private String specimenKey;
	private String createdByKey;
	private String createdBy;
}

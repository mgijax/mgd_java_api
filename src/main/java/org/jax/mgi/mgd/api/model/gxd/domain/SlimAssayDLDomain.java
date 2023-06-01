package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAssayDLDomain extends BaseDomain {

	// a slim version of info needed by pwi/Assay/Double Label tool
	// which will be used to generate a specimen note
	
	private String specimenKey;
	private String specimenLabel;
	private String assayKey;
	private String assayTypeKey;
	private String assayTypes;
	private String assayExtraWords;
	private String assayIDs;
	private String markers;
}

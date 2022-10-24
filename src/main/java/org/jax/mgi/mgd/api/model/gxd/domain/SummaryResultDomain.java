package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryResultDomain extends BaseDomain {

	// summary of assay results 
	// not to be used when editing purposes
	
	private String assayKey;
	private String accID;
	private String newassaType;
	private String assayTypeKey;
	private String assayType;
	private String markerKey;
	private String markerSymbol;
	private String refsKey;
	private String jnumid;
	private String age;
	private String structure;
	private String cellTypeKey;
	private String cellType;
	private String expressed;
	private String strength;
	private String specimenLabel;
	private String resultNote;
	private String alleleKey1;
	private String alleleKey2;
	private String alleleSymbol1;
	private String alleleSymbol2;
}

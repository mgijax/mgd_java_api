package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryResultDomain extends BaseDomain {

	// summary of assay results 
	// not to be used when editing purposes
	
	// input parameters
	private String jnumid;
	private String offset;
	private String limit;
	
	// output results
	private String expressionKey;
	private String refsKey;	
	private String assayKey;
	private String assayID;
	private String assayTypeKey;
	private String assayType;
	private Integer assayTypeSequenceNum;
	private String markerKey;
	private String markerID;
	private String markerSymbol;
	private String age;
	private String structureID;
	private String structure;
	private String cellTypeID;
	private String cellTypeKey;
	private String cellType;
	private String strength;
	private String resultNote;
	private String specimenLabel;
	private String alleleDetailNote;
}

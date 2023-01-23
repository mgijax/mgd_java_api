package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryResultDomain extends BaseDomain {

	// summary of assay results 
	// not to be used when editing purposes
	
	// input parameters
	private String offset;
	private String limit;
	private String cellTypeID;	
	private String jnumid;
	private String markerID;
	private String structureID;

	// output results
	private String expressionKey;
	private Integer stageKey;
	private String refsKey;	
	private String assayKey;
	private String assayID;
	private String assayTypeKey;
	private String assayType;
	private Integer assayTypeSequenceNum;
	private String markerKey;
	private String markerSymbol;
	private String age;
	private String structure;
	private String cellTypeKey;
	private String cellType;
	private String strength;
	//private String pattern;
	private String resultNote;
	private String specimenLabel;
	private String alleleDetailNote;
}

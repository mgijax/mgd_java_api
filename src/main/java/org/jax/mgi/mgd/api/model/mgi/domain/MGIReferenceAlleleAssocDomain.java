package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAlleleAssocDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String objectKey;
	private String mgiTypeKey;
	private String refAssocTypeKey;
	private String refAssocType;
	private String refsKey;
//	private String createdByKey;
//	private String createdBy;
//	private String modifiedByKey;
//	private String modifiedBy;
//	private String creation_date;
//	private String modification_date;
	
	// allele assoc info
	private String symbol;
	private String accID;
	private String markerSymbol;
	
}   	
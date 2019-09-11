package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAlleleAssocDomain extends BaseDomain {

	// for returning allele-associations
	
	private String processStatus;
	private String assocKey;
	private String objectKey;
	private String mgiTypeKey;
	private String refAssocTypeKey;
	private String refAssocType;
	private String refsKey;
	private String alleleSymbol;
	private String alleleAccID;
	private String alleleMarkerSymbol;
	
}   	
package org.jax.mgi.mgd.api.model.acc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Slim Accession Domain")
public class SlimAccessionDomain extends BaseDomain {

	// a slim version of AccessionDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String accessionKey;
	private String logicaldbKey;
	private String logicaldbName;
	private String objectKey;
	private String mgiTypeKey;
	private String mgiTypeName;
	private String accID;
	private String prefixPart;
	private String numericPart;
	private String symbol;
		
}

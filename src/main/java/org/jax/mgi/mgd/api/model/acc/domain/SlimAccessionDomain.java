package org.jax.mgi.mgd.api.model.acc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Slim Accession Domain")
public class SlimAccessionDomain extends BaseDomain {

	// a slim version of AccessionDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String accessionKey;
	private String logicaldbKey;
	private String objectKey;
	private String mgiTypeKey;
	private String accID;
	private String prefixPart;
	private String numericPart;
	//private String isPrivate;
	//private String preferred;
		
}

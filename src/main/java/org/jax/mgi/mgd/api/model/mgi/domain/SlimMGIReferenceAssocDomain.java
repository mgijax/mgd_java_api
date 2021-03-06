package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMGIReferenceAssocDomain extends BaseDomain {

	// a slim version of MGIReferenceAssocDomain 
	// not to be used when editing purposes
	// to be used for returning search results
		
	private String assocKey;
	private String objectKey;
	private String mgiTypeKey;
	private String refAssocTypeKey;
	private String refAssocType;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;

}   	
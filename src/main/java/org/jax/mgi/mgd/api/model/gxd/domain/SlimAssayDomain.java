package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAssayDomain extends BaseDomain {

	// a slim version of AssayDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String assayKey;
	private String assayDisplay;	
	private String assayTypeKey;
	private String assayType;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String createdByKey;
	private String createdBy;
}

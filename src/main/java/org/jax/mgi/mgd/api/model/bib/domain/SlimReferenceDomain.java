package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimReferenceDomain extends BaseDomain {

	// a slim version of ReferenceDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String journal;

	// used by validateJnumImage
	private String copyright;
	private Boolean needsDXDOIid = false;
	private Boolean isCreativeCommons = false;
}

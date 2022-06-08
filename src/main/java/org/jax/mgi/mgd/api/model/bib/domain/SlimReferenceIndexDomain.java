package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimReferenceIndexDomain extends BaseDomain {

	// a slim version of ReferenceDomain with GXD Index info 
	
	public String refsKey;
	public String jnumid;
	public String jnum;
	public String short_citation;
	private String priorityKey;
	private String priority;
	private String conditionalMutantsKey;
	private String conditionalMutants;
}

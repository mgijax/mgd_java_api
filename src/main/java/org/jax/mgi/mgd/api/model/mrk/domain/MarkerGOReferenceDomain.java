package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerGOReferenceDomain extends BaseDomain {
	// marker references not coded for GO
	
	private String refsKey;
	private String jnum;
	private String jnumid;
	private String short_citation;	
	
}

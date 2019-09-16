package org.jax.mgi.mgd.api.model.mgi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAlleleAssocDomain extends MGIReferenceAssocDomain {

	private String alleleSymbol;
	private String alleleAccID;
	private String alleleMarkerSymbol;
		
}   	
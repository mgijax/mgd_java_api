package org.jax.mgi.mgd.api.model.mgi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceDOIDAssocDomain extends MGIReferenceAssocDomain {

	private String doidTerm;
	private String doidAccID;
		
}
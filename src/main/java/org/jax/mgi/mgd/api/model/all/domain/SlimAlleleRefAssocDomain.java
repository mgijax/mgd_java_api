package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleRefAssocDomain extends BaseDomain {
	// used to help display basic allele info for handling MGI_Reference_Assoc
	
	private String alleleKey;
	private String symbol;
	private List<SlimAccessionDomain> mgiAccessionIds;
}

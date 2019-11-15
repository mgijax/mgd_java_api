package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleAnnotDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;	
	private List<SlimAccessionDomain> mgiAccessionIds;

}

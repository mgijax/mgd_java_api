package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;
	private String markerKey;
	private String markerSymbol;
	private String chromosome;
	private String strand;
	private String accID;
	private List<SlimMGIReferenceAssocDomain> refAssocs;	
}

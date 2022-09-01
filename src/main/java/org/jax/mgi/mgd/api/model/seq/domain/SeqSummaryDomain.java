package org.jax.mgi.mgd.api.model.seq.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SeqSummaryDomain extends BaseDomain {
		
	private String accID;
	private String sequenceType;
	private String length;
	private String rawStrain;
	private String markerSymbol;
	private String description;
}

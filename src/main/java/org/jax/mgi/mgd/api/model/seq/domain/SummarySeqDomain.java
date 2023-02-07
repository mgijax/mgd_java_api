package org.jax.mgi.mgd.api.model.seq.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummarySeqDomain extends BaseDomain {
		
	private String accID;
	private String url;
	private String sequenceType;
	private String length;
	private String strain;
	private String markers; // comma separated list of "symbol|mgiid"
	private String description;
}

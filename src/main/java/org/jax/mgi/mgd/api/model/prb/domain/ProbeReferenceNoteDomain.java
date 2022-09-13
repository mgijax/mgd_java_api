package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeReferenceNoteDomain extends BaseDomain {

	private String referenceKey;
	private String note;
	private String creation_date;
	private String modification_date;
}

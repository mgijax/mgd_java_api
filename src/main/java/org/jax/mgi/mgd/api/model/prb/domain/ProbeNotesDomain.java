package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeNotesDomain extends BaseDomain {

	private String processStatus;
	private String noteKey;
	private String probeKey;
	private String notes;
	private String creation_date;
	private String modification_date;
}

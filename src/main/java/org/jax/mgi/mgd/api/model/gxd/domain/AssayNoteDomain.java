package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayNoteDomain extends BaseDomain {

	private String processStatus;
	private String assayNoteKey;
	private String assayKey;
	private String assayNote;
	private String creation_date;
	private String modification_date;

}

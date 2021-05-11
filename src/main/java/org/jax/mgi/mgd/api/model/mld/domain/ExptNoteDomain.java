package org.jax.mgi.mgd.api.model.mld.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExptNoteDomain extends BaseDomain {

	private String processStatus;	
	private String exptKey;
	private String note;
	private String creation_date;
	private String modification_date;

}

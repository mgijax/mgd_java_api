package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceNoteDomain extends BaseDomain {
	
	private String processStatus;	
	private String refsKey;
	private String note;	
	private String creation_date;
	private String modification_date;

}

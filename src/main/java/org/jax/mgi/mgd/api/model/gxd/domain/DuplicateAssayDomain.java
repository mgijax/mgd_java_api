package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DuplicateAssayDomain extends BaseDomain {

	// 1 = All
	// 2 = Patrial
	// 3 = Prep
	private String duplicateType;	
	private String assayKey;
	private String createdByKey;
}

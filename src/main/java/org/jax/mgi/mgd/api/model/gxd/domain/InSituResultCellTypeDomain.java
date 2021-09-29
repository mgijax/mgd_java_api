package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InSituResultCellTypeDomain extends BaseDomain {

	private String processStatus;
	private String resultCelltypeKey;
	private String resultKey;
	private String celltypeTermKey;
	private String celltypeTerm;
	private String creation_date;
	private String modification_date;
		
}

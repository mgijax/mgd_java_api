package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GXDIndexStageDomain extends BaseDomain {

	private String processStatus;
	private String indexStageKey;
	private String indexKey;
	private String indexAssayKey;
	private String indexAssay;
	private String stageidKey;
	private String stageid;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}

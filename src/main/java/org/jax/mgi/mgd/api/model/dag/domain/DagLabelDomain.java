package org.jax.mgi.mgd.api.model.dag.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "DagLabel Domain")
public class DagLabelDomain extends BaseDomain {

	private String labelKey;
	private String label;
	private String creation_date;
	private String modification_date;
}

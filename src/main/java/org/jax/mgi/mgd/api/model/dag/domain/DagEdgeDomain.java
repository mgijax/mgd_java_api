package org.jax.mgi.mgd.api.model.dag.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "DagEdge Domain")
public class DagEdgeDomain extends BaseDomain {

	private String edgeKey;
	private String dagKey;
	private String parentKey;
	private String parentTerm;
	private String childKey;
	private String childTerm;
	private String sequenceNum;
	private String creation_date;
	private String modification_date;
	private DagLabelDomain label;
}

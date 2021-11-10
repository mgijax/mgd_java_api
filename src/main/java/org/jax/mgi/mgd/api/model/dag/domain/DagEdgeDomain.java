package org.jax.mgi.mgd.api.model.dag.domain;

import javax.persistence.Id;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "DagEdge Domain")
public class DagEdgeDomain extends BaseDomain {

	@Id
	private String edgeKey;
	private String dagKey;
	private String parentKey;
	private String childKey;
	private String sequenceNum;
	private String creation_date;
	private String modification_date;
	private DagLabelDomain label;
}

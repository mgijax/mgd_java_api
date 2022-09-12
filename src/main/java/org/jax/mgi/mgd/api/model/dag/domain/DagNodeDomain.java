package org.jax.mgi.mgd.api.model.dag.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "DagNode Domain")
public class DagNodeDomain extends BaseDomain {
	
	private String nodeKey;
	private String objectKey;	
	private String term;
	private String creation_date;
	private String modification_date;
	private DagDomain dagDomain;
	private DagLabelDomain label;
	private List<DagEdgeDomain> parentEdges;
	private List<DagEdgeDomain> childEdges;
}

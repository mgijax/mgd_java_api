package org.jax.mgi.mgd.api.model.dag.domain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "DagNode Domain")
public class DagNodeDomain extends BaseDomain {
	
	private String nodeKey;
	private String objectKey;	
	private String objectTerm;
	private String creation_date;
	private String modification_date;
	private DagDomain dagDomain;
	private DagLabelDomain label;
	private List<DagEdgeDomain> parentEdges;
	private List<DagEdgeDomain> childEdges;
	private List<DagEdgeDomain> siblingEdges;
}

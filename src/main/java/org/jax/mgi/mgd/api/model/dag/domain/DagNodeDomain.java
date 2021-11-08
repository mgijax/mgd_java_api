package org.jax.mgi.mgd.api.model.dag.domain;

import java.util.Date;

import javax.persistence.Id;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "DagNode Domain")
public class DagNodeDomain extends BaseDomain {
	
	@Id
	private String nodeKey;
	private String objectKey;	
	
	private Date creation_date;
	private Date modification_date;
	
	private DagDomain dagDomain;

	private DagLabelDomain label;
	
}


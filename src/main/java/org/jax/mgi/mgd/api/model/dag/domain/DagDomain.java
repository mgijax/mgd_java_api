package org.jax.mgi.mgd.api.model.dag.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Dag Domain")
public class DagDomain extends BaseDomain{

	private String dagKey;
	private String refsKey;
	private String name;
	private String abbreviation;
	private String creation_date;
	private String modification_date;
	private MGIType mgiType;
}	

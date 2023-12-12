package org.jax.mgi.mgd.api.model.dag.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Dag Domain")
public class DagDomain extends BaseDomain{

	private String dagKey;
	private String refsKey;
	private String mgiTypeKey;
	private String name;
	private String abbreviation;
	private String creation_date;
	private String modification_date;
}	

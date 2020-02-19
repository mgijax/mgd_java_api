package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyAliasDomain extends BaseDomain {

	private String processStatus;	
	private String antibodyAliasKey;
	private String antibodyKey;
	private String alias;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;	
	private String creation_date;
	private String modification_date;
}

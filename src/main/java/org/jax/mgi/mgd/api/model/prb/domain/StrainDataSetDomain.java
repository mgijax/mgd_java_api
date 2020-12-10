package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrainDataSetDomain extends BaseDomain {
	// for returning stored procedure results
	// see sp/PRB_getStrainReferences
	
	private String refsKey;
	private Integer jnum;
	private String jnumid;
	private String short_citation;	
	private String dataSet;
	
}

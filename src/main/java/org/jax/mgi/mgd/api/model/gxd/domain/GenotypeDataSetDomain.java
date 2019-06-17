package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeDataSetDomain extends BaseDomain {
	// for returning stored procedure results
	// see sp/GXD_getGenotypesDataSets
	
	private String jnumid;
	private String short_citation;	
	private String dataSet;
	
}

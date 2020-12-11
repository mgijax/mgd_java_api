package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrainDataSetDomain extends BaseDomain {
	// for returning stored procedure results
	// see sp/PRB_getStrainReferences
	// see ap/PRB_getStrainDataSets
	
	private Integer jnum;
	private String accID;
	private String dataSet;
	
}

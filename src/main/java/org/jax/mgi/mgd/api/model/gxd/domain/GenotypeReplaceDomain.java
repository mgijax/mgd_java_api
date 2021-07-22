package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeReplaceDomain extends BaseDomain {
	// for returning stored procedure results
	// see sp/select * from GXD_replaceGenotype (userKey, refsKey, currentGenotypeKey, newGenotypeKey)
	
	private String createdBy;
	private String refsKey;
	private Integer jnum;
	private String jnumid;
	private String short_citation;	
    private String currentKey;
    private String currentDisplay;
    private String currentAccID;
    private String newKey;
    private String newDisplay;
    private String newAccID;
	
}

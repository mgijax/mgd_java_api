package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Slim Genotype DO Domain")
public class SlimGenotypeDODomain extends BaseDomain {
	// this domain is used by get() to determine if DO annotations are duplicates
	
	private String termKey;
	private String qualifierKey;
	private String evidenceTermKey;
	private String refsKey;
	private String jnumid;
	
}

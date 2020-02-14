package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Slim Allele DO Domain")
public class SlimAlleleDODomain extends BaseDomain {
	// this domain is used by get() to determine if DO annotations are duplicates
	
	private String termKey;
	private String qualifierKey;
	private String evidenceTermKey;
	private String refsKey;
	private String jnumid;
	
}

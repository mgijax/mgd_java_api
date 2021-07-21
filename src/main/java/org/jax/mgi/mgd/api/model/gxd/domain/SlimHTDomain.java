package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimHTDomain extends BaseDomain {

	// a slim version of AssayDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String _curationstate_key;
	private String _experiment_key;	
	private String primaryid;
}

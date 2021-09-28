package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTEmapsDomain extends BaseDomain {

	private String primaryid;
	private Integer _emapa_term_key;
	private Integer _stage_key;
	private Integer _term_key;
	private HTEmapaDomain emapa_term;

}

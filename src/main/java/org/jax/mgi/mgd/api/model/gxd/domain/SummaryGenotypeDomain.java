package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryGenotypeDomain extends BaseDomain {

	private String jnumid;	
	private String createdBy;
	private String genotypeid;
	private String genotypeBackground;
	private String alleleDetailNote;
	private Boolean isConditional;
	private Boolean hasAssay;
	private Boolean hasMPAnnot;
	private Boolean hasDOAnnot;
}

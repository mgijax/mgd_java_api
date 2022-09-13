package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeAlleleDomain extends BaseDomain {

	private String alleleKey;
	private String rflvKey;
	private String allele;
	private String fragments;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	List<ProbeAlleleStrainDomain> alleleStrains;
}

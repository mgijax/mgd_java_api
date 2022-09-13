package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeRFLVDomain extends BaseDomain {

	private String rflvKey;
	private String referenceKey;
	private String markerKey;
	private String symbol;
	private String endonuclease;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	List<ProbeAlleleDomain> rflvAlleles;
} 

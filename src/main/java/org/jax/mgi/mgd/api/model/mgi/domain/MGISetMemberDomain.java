package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberDomain extends BaseDomain {

	private String processStatus;
	private String setMemberKey;
	private String setKey;
	private String objectKey;
	private String label;
	private Integer sequenceNum;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private String genotypeID;
	private String celltypeID;
	
}   	
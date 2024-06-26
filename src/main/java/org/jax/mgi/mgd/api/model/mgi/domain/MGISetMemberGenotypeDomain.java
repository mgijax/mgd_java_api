package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberGenotypeDomain extends BaseDomain {

	private String setKey;
	private String setMemberKey;
	private String objectKey;
	private String label;
	private String displayIt;
	private String strain;
	private String alleleDetailNote;
	private String isConditional;
	private String createdByKey;
	private String createdBy;	
	
}   	
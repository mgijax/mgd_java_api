package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberEmapaDomain extends BaseDomain {

	private String setKey;
	private String setMemberKey;
	private String displayIt;
	private String term;
	private String stage;
	private String createdByKey;
	private String createdBy;	
	
}   	
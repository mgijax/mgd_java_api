package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberEmapaDomain extends BaseDomain {

	private String processStatus = Constants.PROCESS_NOTDIRTY;
	private String setMemberEmapaKey;
	private String setMemberKey;
	private String setKey;	
	private String objectKey;
	private String displayIt;
	private String term;
	private String stageKey;
	private String stage;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;	
	private Boolean isUsed = false;

}   	
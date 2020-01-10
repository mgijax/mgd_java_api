package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetDomain extends BaseDomain {

	private String processStatus;
	private String setKey;
	private String mgiTypeKey;
	private String setName;
	private String sequenceNum;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private List<MGISetMemberDomain> emapaStageMembers;
	private List<MGISetMemberDomain> genotypeClipboardMembers;	
	
}   	
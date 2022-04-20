package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceWorkflowStatusDomain extends BaseDomain {

	private String refsKey;
	private boolean isCurrent;
	private String groupKey;
	private String group;
	private String group_abbreviation;
	private String statusKey;
	private String status;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
	
	//public LTReferenceWorkflowStatusDomain() {}

//	public LTReferenceWorkflowStatusDomain(LTReferenceWorkflowStatus rws) {
//		this.refsKey = rws.getRefsKey();
//		this.creation_date = rws.getCreationDate();
//		this.modification_date = rws.getModificationDate();
//		this.createdby_user = rws.getCreatedByUser().getLogin();
//		this.modifiedby_user = this.createdby_user;
//		this.group = rws.getGroup();
//		this.group_abbreviation = rws.getGroupAbbreviation();
//		this.status = rws.getStatus();
//		this.isCurrent = true;
//		if (rws.getIsCurrent() == 0) {
//			this.isCurrent = false;
//		}
//	}
}

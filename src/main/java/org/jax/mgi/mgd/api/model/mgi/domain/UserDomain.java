package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDomain extends BaseDomain {

	private String processStatus;
	private String userKey;
	private String userTypeKey;
	private String userStatusKey;
	private String userLogin;
	private String userName;
	private String orcid;
	private String groupKey;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

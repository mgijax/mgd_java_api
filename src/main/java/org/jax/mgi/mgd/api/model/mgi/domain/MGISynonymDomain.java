package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISynonymDomain extends BaseDomain {

	private String processStatus;
	private String synonymKey;
	private String objectKey;
	private String mgiTypeKey;
	private String synonymTypeKey;
	private String synonymType;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String synonym;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}   	
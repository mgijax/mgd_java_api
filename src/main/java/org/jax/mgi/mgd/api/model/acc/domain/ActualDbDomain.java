package org.jax.mgi.mgd.api.model.acc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ActualDbDomain extends BaseDomain {
	
	private String actualDBKey;
	private String logicalDBKey;
	private String name;
	private String active;
	private String url;
	private String allowsMultiple;
	private String delimiter;
	
	private String createdBy;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

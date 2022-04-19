package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipPropertyDomain extends BaseDomain {

	private String processStatus;
	private String relationshipPropertyKey;	
	private String relationshipKey;
	private String propertyNameKey;
	private String propertyName;
	private String value;
	private Integer sequenceNum;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;

}   	
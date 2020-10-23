package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrganismMGITypeDomain extends BaseDomain {

	private String processStatus;	
	private String assocKey;
	private String organismKey;
	private String mgiTypeKey;
	private String mgiType;
	private Integer sequenceNum;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
	
}

package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Reference Association Type Model Object")
public class MGIRefAssocTypeDomain extends BaseDomain {

	private String refAssocTypeKey;
	private String mgiTypeKey;
	private String assocType;
	private String allowOnlyOne;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}   	
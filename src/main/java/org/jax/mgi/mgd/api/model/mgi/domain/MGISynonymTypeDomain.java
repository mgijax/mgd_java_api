package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Synonym Type Model Object")
public class MGISynonymTypeDomain extends BaseDomain {

	private String synonymTypeKey;
	private String synonymType;
	private String definition;
	private String allowOnlyOne;
	private String mgiTypeKey;
	private String organismKey;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}   	
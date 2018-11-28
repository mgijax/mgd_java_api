package org.jax.mgi.mgd.api.model.acc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "AccessionReference Domain")
public class AccessionReferenceDomain extends BaseDomain {

	private String accessionKey;
	private String refKey;
	private String jnumid;
	private String short_citation;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}

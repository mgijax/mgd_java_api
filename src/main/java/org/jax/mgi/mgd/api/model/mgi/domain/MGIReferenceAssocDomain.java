package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "MGI Reference Association Model Object")
public class MGIReferenceAssocDomain extends BaseDomain {

	private String assocKey;
	private String objectKey;
	private String mgiTypeKey;
	private String refAssocTypeKey;
	private String refAssocType;
	private String refKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}   	
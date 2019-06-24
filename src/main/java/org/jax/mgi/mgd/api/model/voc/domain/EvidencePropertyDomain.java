package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Evidence Property Domain")
public class EvidencePropertyDomain extends BaseDomain {

	private String processStatus;
	private String evidencePropertyKey;
	private String annotevidenceKey;
	private String propertyTermKey;
	private String propertyTerm;
	private String stanza;
	private String sequenceNum;
	private String value;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

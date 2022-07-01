package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Annotation Header Domain")
public class AnnotationHeaderDomain extends BaseDomain {

	private String processStatus;
	private String annotHeaderKey;
	private String annotTypeKey;
	private String annotType;
	private String objectKey;
	private String termKey;
	private String term;
	private Integer termSequenceNum;
	private Integer sequenceNum;
	private String isNormal;
	private String approvedByKey;
	private String approvedBy;
	private String approval_date;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

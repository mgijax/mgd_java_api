package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Annotation Domain")
public class AnnotationDomain extends BaseDomain {

	private String processStatus;
	private String annotKey;
	private String annotTypeKey;
	private String annotType;
	private String objectKey;
	private String termKey;
	private String term;
	private String qualifierKey;
	private String qualifier;
	private String creation_date;
	private String modification_date;
	
	EvidenceDomain evidence;
	
}

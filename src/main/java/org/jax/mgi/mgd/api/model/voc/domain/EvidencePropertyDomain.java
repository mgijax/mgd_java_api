package org.jax.mgi.mgd.api.model.voc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Evidence Property Domain")
public class EvidencePropertyDomain extends BaseDomain {

	private String processStatus;
	private String evidencePropertyKey;
	private String annotEvidenceKey;
	private String propertyTermKey;
	private String propertyTerm;
	private Integer stanza;
	private Integer sequenceNum;
	private String value;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

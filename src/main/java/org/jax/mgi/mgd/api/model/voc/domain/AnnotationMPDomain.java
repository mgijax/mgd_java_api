package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Annotation MP (denormalized) Domain")
public class AnnotationMPDomain extends BaseDomain {
	// works with the GenotypeMPDenormDomain
	
	private String processStatus;
	private String annotKey;
	private String annotTypeKey;
	private String annotType;
	private String objectKey;
	private String termKey;
	private String term;
	private String qualifierKey;
	private String qualifierAbbreviation;
	private String qualifier;
	private String mpid;	

	// evidence
	private String annotEvidenceKey;
	private String evidenceTermKey;
	private String evidenceTerm;
	private String evidenceAbbreviation;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	// evidence-property : sex-specificity
	private String evidencePropertyKey;
	private String propertyTermKey;
	private String mpSexSpecificityValue;
	
}

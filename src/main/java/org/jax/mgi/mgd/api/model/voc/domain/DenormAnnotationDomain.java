package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Annotation (denormalized) Domain")
public class DenormAnnotationDomain extends BaseDomain {
	
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
	private String termid;	

	// evidence
	private String annotEvidenceKey;
	private String evidenceTermKey;
	private String evidenceTerm;
	private String evidenceAbbreviation;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	// evidence-properties
	private List<EvidencePropertyDomain> properties;
	
	// all notes for given evidence
	private List<NoteDomain> allNotes;

}

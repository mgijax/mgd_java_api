package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeAnnotHeaderViewDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Annotation (denormalized) Domain")
public class DenormAnnotationDomain extends BaseDomain {
	
	private String processStatus;
	private String annotKey;
	private String annotTypeKey;
	private String annotType;
	private String objectKey;
	private String termKey;
	private String term;
	private Integer termSequenceNum;	
	private String goDagAbbrev;
	private String qualifierKey;
	private String qualifierAbbreviation;
	private String qualifier;
	private String termid;	

	// evidence
	private String annotEvidenceKey;
	private String evidenceTermKey;
	private String evidenceTerm;
	private String evidenceAbbreviation;
	private String inferredFrom;
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
	
	// properties
	private List<EvidencePropertyDomain> properties;
	private Boolean hasProperties = false;
	
	// notes for given evidence
	private List<NoteDomain> allNotes;
	
	// is annotation a duplicate?
	Boolean isDuplicate = false;
	
	// for genotype/mp only
	private List<GenotypeAnnotHeaderViewDomain> headersByAnnot;	

}

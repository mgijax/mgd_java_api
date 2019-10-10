package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Evidence Domain")
public class EvidenceDomain extends BaseDomain {

	private String processStatus;
	private String annotEvidenceKey;
	private String annotKey;
	private String evidenceTermKey;
	private String evidenceTerm;
	private String evidenceAbbreviation;
	private String inferredFrom;
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
	
	private List<NoteDomain> allNotes;
	
	private List<EvidencePropertyDomain> mpSexSpecificity;
	
}

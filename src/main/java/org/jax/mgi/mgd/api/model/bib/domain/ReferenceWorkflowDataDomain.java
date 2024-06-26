package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceWorkflowDataDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String refsKey;
	private String haspdf;
	private String supplementalKey;
	private String supplementalTerm;

	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
//	private String linksupplemental;
//	private String extractedtext;	
//	private String extractedTextKey;
//	private String extractedTextTerm;		
}

package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipDomain extends BaseDomain {

	private String processStatus;
	private String relationshipKey;
	private String categoryKey;
	private String categoryTerm;
	private String objectKey1;
	private String object1;
	private String objectKey2;
	private String object2;
	private String relationshipTermKey;
	private String relationshipTerm;
	private String qualifierKey;
	private String qualifierTerm;
	private String evidenceKey;
	private String evidenceTerm;
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
	
	//private List<MarkerDomain> tssToMarker;
	//private List<MarkerDomain> geneToMarker;
	
}   	
package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipFearByMarkerDomain extends BaseDomain {
	// similar to RelationshipDomain
	// except...
	// 			objectKey1 = markerKey1, markerSymbol1
	// 			objectKey2 = markerKey2, markerSymbol2
	
	private String processStatus;
	private String relationshipKey;
	private String categoryKey;
	private String categoryTerm;
	private String markerKey1;
	private String markerSymbol1;
	private String markerKey2;
	private String markerSymbol2;
	private String markerAccID2;
	private String organismKey;
	private String organism;
	private String relationshipTermKey;
	private String relationshipTerm;
	private String qualifierKey;
	private String qualifierTerm;
	private String evidenceKey;
	private String evidenceTerm;
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
	private NoteDomain note;
}
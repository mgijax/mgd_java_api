package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

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
	private String objectKey2;
	
	// these are specific to the category/mgi-types
	// so a specific entity/domain is needed to map the category/mgi-type to its specific master.
	//private String object1;
	//private String object2;
	
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
	private List<RelationshipPropertyDomain> properties;
}   	
package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipFEARDomain extends BaseDomain {

	private String processStatus;
	private String relationshipKey;
	private String categoryKey;
	private String categoryTerm;
	private String alleleKey;
	private String alleleSymbol;
	private String markerKey;
	private String markerSymbol;
	private String relationshipterm_key;
	private String relationshipTerm;
	private String qualifierKey;
	private String qualifierTerm;
	private String evidence_key;
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

	List<RelationshipPropertyDomain> properties;

}   	
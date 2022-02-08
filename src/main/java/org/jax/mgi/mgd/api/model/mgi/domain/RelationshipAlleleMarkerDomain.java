package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipAlleleMarkerDomain extends BaseDomain {

	private String processStatus;
	private String relationshipKey;
	private String alleleKey;
	private String markerKey;
	private String organismKey;
	private String markerSymbol;	
	private String commonname;
	private String accID;

	List<RelationshipPropertyDomain> properties;

}   	
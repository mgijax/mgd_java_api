package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipMarkerPARDomain extends BaseDomain {

	private String relationshipKey;
	private String objectKey1;
	private String symbol;

}   	
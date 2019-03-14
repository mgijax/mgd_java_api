package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RelationshipMarkerTSSDomain extends BaseDomain {

	private String relationshipKey;
	private String objectKey1;
	private String objectKey2;
	private String symbol1;
	private String symbol2;

}   	
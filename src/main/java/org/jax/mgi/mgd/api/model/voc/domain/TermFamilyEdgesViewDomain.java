package org.jax.mgi.mgd.api.model.voc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "TermFamilyEdgesView Domain")
public class TermFamilyEdgesViewDomain extends BaseDomain {

	private String edgeKey;
	private String childKey;
	private String parentKey;
	private String searchid;
	private String label;
	
}


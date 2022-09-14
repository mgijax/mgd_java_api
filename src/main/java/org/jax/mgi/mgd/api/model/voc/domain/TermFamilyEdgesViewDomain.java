package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "TermFamilyEdgesView Domain")
public class TermFamilyEdgesViewDomain extends BaseDomain {

	private String edgeKey;
	private String childKey;
	private String parentKey;
	private String accid;	
	private String label;
	
}


package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIRefAssocTypeDomain extends BaseDomain {

	private String refAssocTypeKey;
	private String mgiTypeKey;
	private String assocType;
	private String allowOnlyOne;
	private String createdByKey;
	private String modifiedByKey;
	private String creation_date;
	private String modification_date;
}   	
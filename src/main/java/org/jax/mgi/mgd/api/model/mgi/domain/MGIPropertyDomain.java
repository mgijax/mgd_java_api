package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIPropertyDomain extends BaseDomain {

	private String processStatus;

	private String propertyKey;
	private String propertyTermKey;
	private String propertyTypeKey;
	private String objectKey;
	private String mgiTypeKey;
	private String value;
	private String sequenceNum;
}  
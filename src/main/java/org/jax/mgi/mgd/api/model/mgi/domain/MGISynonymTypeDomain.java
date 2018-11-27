package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISynonymTypeDomain extends BaseDomain {

	private String synonymTypeKey;
	private String synonymType;
	private String definition;
	private String allowOnlyOne;
	private String mgiTypeKey;
	private String organismKey;
	private String createdByKey;
	private String modifiedByKey;
	private String creation_date;
	private String modification_date;
}   	
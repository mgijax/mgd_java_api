package org.jax.mgi.mgd.api.model.mld.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MappingAssayTypeDomain extends BaseDomain {

	private String assayTypeKey;
	private String description;
	private String creation_date;
	private String modification_date;

}

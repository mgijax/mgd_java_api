package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyTypeDomain extends BaseDomain {

	private String antibodyTypeKey;
	private String antibodyType;
	private String creation_date;
	private String modification_date;
}

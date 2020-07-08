package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyClassDomain extends BaseDomain {

	private String antibodyClassKey;
	private String antibodyClass;
	private String creation_date;
	private String modification_date;
}

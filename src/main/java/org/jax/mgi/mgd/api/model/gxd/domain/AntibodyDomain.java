package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyDomain extends BaseDomain {

	private Integer _antibody_key;
	private String antibodyName;
	private String antibodyNote;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
}

package org.jax.mgi.mgd.api.model.mld.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExperimentDomain extends BaseDomain {

	private Integer _expt_key;
	private String exptType;
	private Integer tag;
	private String chromosome;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
}

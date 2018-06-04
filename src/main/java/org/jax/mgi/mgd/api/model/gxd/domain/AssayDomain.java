package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.Date;

import javax.persistence.Id;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayDomain extends BaseDomain {

	private Integer _assay_key;
	private Date creation_date;
	private Date modification_date;
}

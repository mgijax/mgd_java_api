package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayDomain extends BaseDomain {

	private Integer _assay_key;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	
	private List<SpecimenDomain> specimens;
	private List<GelLaneDomain> gellanes;

}

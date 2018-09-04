package org.jax.mgi.mgd.api.model.all.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleDomain extends BaseDomain {

	private Integer _allele_key;
	private String symbol;
	private String name;
	private Integer isWildType;
	private Integer isExtinct;
	private Integer isMixed;
	private String createdBy;
	private String modifiedBy;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
}

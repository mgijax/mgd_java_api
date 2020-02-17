package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeDomain extends BaseDomain {

	private String probeKey;
	private String name;
	private String derivedFrom;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	private String accID;
}

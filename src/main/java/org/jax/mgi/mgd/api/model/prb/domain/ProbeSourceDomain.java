package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeSourceDomain extends BaseDomain {

	private String sourceKey;
	private String name;
	private String description;
	private String age;
	private String segmentTypeKey;
	private String segmentType;
	private String vectorKey;
	private String vector;
	private String organismKey;
	private String organism;
	private String strainKey;
	private String strain;
	private String tissueKey;
	private String tissue;
	private String genderKey;
	private String gender;
	private String cellLineKey;
	private String cellLine;		
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}

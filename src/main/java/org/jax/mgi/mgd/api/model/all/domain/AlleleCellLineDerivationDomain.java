package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleCellLineDerivationDomain extends BaseDomain {

	private String processStatus;
	private String derivationKey;
	private String name;
	private String description;
	private String vectorKey;
	private String vector;
	private String vectorTypeKey;
	private String vectorType;
	private String derivationTypeKey;
	private String derivationType;
	private String creatorKey;
	private String creator;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private CellLineDomain parentCellLine;	
	
}

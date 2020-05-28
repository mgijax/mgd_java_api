package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleCellLineDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String alleleKey;
	private String mutantCellLineKey;
	private String mutantCellLine;
	private String isMutant;
	private String cellLineTypeKey;
	private String cellLineType;
	private String strainKey;
	private String strain;
	private String derivationKey;
	private String vectorKey;
	private String vector;
	private String parentCellLineKey;
	private String parentCellLine;
	private String creatorKey;
	private String creator;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}

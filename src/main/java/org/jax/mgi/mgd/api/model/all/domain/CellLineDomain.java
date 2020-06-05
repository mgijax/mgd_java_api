package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CellLineDomain extends BaseDomain {

	private String processStatus;
	private String cellLineKey;
	private String cellLine;
	private String cellLineDisplay;
	private String isMutant;
	private String cellLineTypeKey;
	private String cellLineType;
	private String strainKey;
	private String strain;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;	
	
	private AlleleCellLineDerivationDomain derivation;
	
}

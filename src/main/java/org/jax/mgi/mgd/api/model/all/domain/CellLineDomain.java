package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CellLineDomain extends BaseDomain {

	private String cellLineKey;
	private String cellLine;
	private String isMutant;
	private String cellLineTypeKey;
	private String cellLineType;
	private String derivationKey;
	private String creator;
	private String parentCellLineKey;
	private String parentCellLine;
	private String parentStrainKey;
	private String parentStrain;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

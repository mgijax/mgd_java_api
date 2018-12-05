package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Accession Domain")
public class AccessionDomain extends BaseDomain {

	private String ProcessStatus;
	private String accessionKey;
	private String logicaldbKey;
	private String logicaldb;
	private String objectKey;
	private String mgiTypeKey;
	
	private String accID;
	private String prefixPart;
	private String numericPart;
	private String isPrivate;
	private String preferred;
	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private List<AccessionReferenceDomain> references;

}

package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Accession Domain")
public class AccessionDomain extends BaseDomain {

	private String processStatus;
	private String accessionKey;
	private String logicaldbKey;
	private String logicaldb;
	private String akaLogicaldb;
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

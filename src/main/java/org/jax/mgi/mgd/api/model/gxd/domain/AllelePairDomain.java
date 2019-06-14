package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AllelePairDomain extends BaseDomain {

	private String processStatus;
	private String allelePairKey;
	private String genotypeKey;
	private String alleleKey1;
	private String alleleSymbol1;
	private String alleleKey2;
	private String alleleSymbol2;
	private String markerKey;
	private String markerSymbol;
	private String cellLineKey1;
	private String cellLineKey2;
	private String cellLine1;
	private String cellLine2;
	private String pairStateKey;
	private String pairState;
	private String compoundKey;
	private String compound;
	private String sequenceNum;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}

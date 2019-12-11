package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeStrainDomain extends BaseDomain {

	private String strainKey;
	private String strain;
	private String standard;
	private String isPrivate;
	private String geneticBackground;
	private String speciesKey;
	private String species;
	private String strainTypeKey;
	private String strainType;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;
	
}

package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeStrainDomain extends BaseDomain {

	private String strainKey;
	private String strain;
	private String standard;
	private String is_private;
	private String geneticBackground;
	private String speciesKey;
	private String species;
	private String strainTypeKey;
	private String strainType;	
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
}

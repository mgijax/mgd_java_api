package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryAntibodyDomain extends BaseDomain {

	private String antibodyKey;
	private String antibodyID;
	private String antibodyName;
	private String antibodyClass;
	private String antibodyType;
	private String antibodyOrganism;
	private String antibodyNote;
	private String aliases;
	private String antigenID;
	private String antigenName;
	private String regionCovered;
	private String antigenOrganism;
	private String antigenNote;
	private String markerKey;
	private String markerID;
	private String markerSymbol;
	private String jnumID;
	private String shortCitation;
	
}

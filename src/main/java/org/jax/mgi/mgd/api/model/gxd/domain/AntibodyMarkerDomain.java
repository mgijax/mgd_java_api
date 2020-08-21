package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyMarkerDomain extends BaseDomain {

	private String processStatus;	
	private String antibodyMarkerKey;
	private String antibodyKey;
	private String markerKey;
	private String markerSymbol;
	private String chromosome;
	private String creation_date;
	private String modification_date;
	private String markerMGIID;
}

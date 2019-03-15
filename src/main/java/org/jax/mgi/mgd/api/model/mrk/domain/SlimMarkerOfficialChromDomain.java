package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMarkerOfficialChromDomain extends BaseDomain {

	// a slim marker domain
	// needs 2 official markers w/ chromsomes
	// used by validOfficialChrom service method
	
	private String markerKey1;
	private String symbol1;
	private String chromosome1;
	private String markerKey2;
	private String symbol2;
	private String chromosome2;
  
}

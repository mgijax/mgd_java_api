package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimProbeStrainToolDomain extends BaseDomain {

	private String accID;
	private String strainKey;
	private String strain;
	private String isPrivate;
	private String isPrivateString;
	private List<ProbeStrainMarkerDomain> markers;
	private String alleleString;
}

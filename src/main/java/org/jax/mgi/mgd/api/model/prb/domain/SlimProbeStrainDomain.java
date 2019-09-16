package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimProbeStrainDomain extends BaseDomain {

	private String strainKey;
	private String strain;
	private String isPrivate;
	private List<SlimAccessionDomain> mgiAccessionIds;
	
}

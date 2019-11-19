package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimGenotypeDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private List<SlimAccessionDomain> mgiAccessionIds;
}

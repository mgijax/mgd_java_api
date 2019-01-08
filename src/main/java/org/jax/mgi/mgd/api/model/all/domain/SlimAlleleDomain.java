package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;       
	private List<AccessionDomain> mgiAccessionIds;
}

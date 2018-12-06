package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleEIResultDomain extends BaseDomain {

	private int alleleKey;
	private String symbol;       
}

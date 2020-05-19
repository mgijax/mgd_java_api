package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleMutationDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String alleleKey;
	private String mutationKey;
	private String mutation;
	
}

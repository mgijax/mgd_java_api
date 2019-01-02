package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimOrganismDomain extends BaseDomain {

	private String OrganismKey;
	// for backward compatibility with gxd/ht
	private Integer _organism_key;
	private String commonname;

}

package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrganismDomain extends BaseDomain {

	private String _organism_key;
	private String commonname;
	private String latinname;
	private String fullName;
//	private Date creation_date;
//	private Date modification_date;
	
}

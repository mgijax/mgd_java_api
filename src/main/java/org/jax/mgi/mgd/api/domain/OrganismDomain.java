package org.jax.mgi.mgd.api.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrganismDomain extends DomainBase {

	private Integer _organism_key;
	private String commonname;
	private String latinname;
	private Date creation_date;
	private Date modification_date;
	
}

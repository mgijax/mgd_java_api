package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrganismDomain extends BaseDomain {

	private String _organism_key;
	private String commonname;
	private String latinname;
	private String fullName;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
	
	private List<OrganismMGITypeDomain> mgiTypes;
	private List<ChromosomeDomain> chromosomes;
}

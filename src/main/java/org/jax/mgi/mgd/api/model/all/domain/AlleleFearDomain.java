package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByAlleleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleFearDomain extends BaseDomain {

	private String alleleKey;
	private String alleleSymbol;
	private String accID;
	private List<RelationshipFearByAlleleDomain> mutationInvolves;
	private List<RelationshipFearByAlleleDomain> expressesComponents;
	private List<RelationshipFearByAlleleDomain> driverComponents;
	
}

package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFEARDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleFEARDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;
	private String accID;
	private List<RelationshipFEARDomain> relationships;
}

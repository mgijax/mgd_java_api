package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerFearDomain extends BaseDomain {

	private String markerKey;
	private String markerSymbol;
	private String accID;
	private List<RelationshipFearDomain> clusterHasMember;
	
}

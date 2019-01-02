package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMGITypeDomain extends BaseDomain {

	private String MgiTypeKey;	
	private String name;
	private List<SlimOrganismDomain> organisms;
}

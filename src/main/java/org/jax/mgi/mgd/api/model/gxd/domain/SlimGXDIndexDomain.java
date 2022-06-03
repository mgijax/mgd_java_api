package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimGXDIndexDomain extends BaseDomain {

	private String indexKey;
	private String indexDisplay;
	private String markerKey;
	private String markerSymbol;
	private String markerName;
	private String markerChromosome;
	private String markerAccID;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
}

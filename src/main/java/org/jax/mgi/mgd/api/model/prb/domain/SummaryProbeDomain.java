package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryProbeDomain extends BaseDomain {

	private String probeKey;
	private String name;
	private String probeID;
	private String markerKey;
	private String markerSymbol;
	private String markerID;
	private String segmentType;
	private String primer1Sequence;
	private String primer2Sequence;
	private String organism;
	private String aliases;
	private String jnumIDs;
	private String parentID;
	private String parentName;
}

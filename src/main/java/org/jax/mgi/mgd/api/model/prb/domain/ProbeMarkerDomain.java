package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeMarkerDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String probeKey;
	private String markerKey;
	private String markerSymbol;
	private String markerChromosome;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;
	private String relationship;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}

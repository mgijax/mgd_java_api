package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GXDIndexDomain extends BaseDomain {

	private String indexKey;
	private String indexDisplay;
	private String comments;	
	private String priorityKey;
	private String priority;
	private String conditionalMutantsKey;
	private String conditionalMutants;
	private String markerKey;
	private String markerSymbol;
	private String markerTypeKey;
	private String markerType;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String isFullCoded;
	private List<GXDIndexStageDomain> indexStages;
}

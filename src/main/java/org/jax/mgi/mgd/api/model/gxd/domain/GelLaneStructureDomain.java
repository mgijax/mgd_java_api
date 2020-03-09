package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GelLaneStructureDomain extends BaseDomain {

	private String processStatus;
	private String gelLaneStructureKey;
	private String gelLaneKey;
	private String emapaTermKey;
	private String emapaTerm;
	private String theilerStageKey;
	private String theilerStage;	
	private String creation_date;
	private String modification_date;
		
}

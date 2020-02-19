package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InSituResultDomain extends BaseDomain {

	private String processStatus;
	private String resultKey;
	private String specimenKey;
	private Integer sequenceNum;
	private String strengthKey;
	private String strength;
	private String patternKey;
	private String pattern;
	private String resultNote;
	private String creation_date;
	private String modification_date;
	
	private List<InSituResultStructureDomain> structures;
	
}

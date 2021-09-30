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
	
	// used to display in pwi/assay
	private Integer structuresCount;
	private Integer celltypesCount;
	private Integer imagePanesCount;
	private String imagePanesString;

	private List<InSituResultStructureDomain> structures;
	private List<InSituResultCellTypeDomain> celltypes;	
	private List<InSituResultImageViewDomain> imagePanes;
}

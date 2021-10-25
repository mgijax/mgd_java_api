package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GelLaneDomain extends BaseDomain {

	private String processStatus;
	private String gelLaneKey;
	private String assayKey;
	private String genotypeKey;
	private String genotypeAccID;
	private String genotypeBackground;
	private String genotypeAllelePairs;
	private String genotypeIsConditional;	
	private String gelRNATypeKey;
	private String gelRNAType;
	private String gelControlKey;	
	private String gelControl;
	private Integer sequenceNum;
	private String laneLabel;
	private String sampleAmount;
	private String sex;
	private String agePrefix;
	private String ageStage;
	private String age;
	private String ageMin;
	private String ageMax;
	private String ageNote;
	private String laneNote;
	private String creation_date;
	private String modification_date;

	private Integer structuresCount;	
	private List<GelLaneStructureDomain> structures;
	private List<GelBandDomain> gelBands;
	
}

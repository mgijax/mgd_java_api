package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpecimenDomain extends BaseDomain {

	private String processStatus;
	private String specimenKey;
	private String assayKey;
	private String embeddingKey;
	private String embeddingMethod;
	private String fixationKey;
	private String fixationMethod;
	private String genotypeKey;
	private String genotypeAccID;
	private String genotypeBackground;
	private String genotypeAllelePairs;
	private String genotypeIsConditional;
	private Integer sequenceNum;
	private String specimenLabel;
	private String sex;
	private String agePrefix;
	private String ageStage;
	private String age;
	private String ageMin;
	private String ageMax;
	private String ageNote;
	private String hybridization;
	private String specimenNote;
	private String creation_date;
	private String modification_date;
	
	private Integer sresultsCount = 0;
	private List<InSituResultDomain> sresults;
	
}

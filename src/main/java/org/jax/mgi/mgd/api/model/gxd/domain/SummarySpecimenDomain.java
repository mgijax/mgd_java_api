package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummarySpecimenDomain extends BaseDomain {

	private String specimenKey;
	private String assayID;
	private String assayType;
	private String embeddingMethod;
	private String fixationMethod;
	private String genotypeBackground;
	private String genotypeAllelePairs;
	private String specimenLabel;
	private String sex;
	private String age;
	private String ageNote;
	private String hybridization;
	private String specimenNote;
		
}

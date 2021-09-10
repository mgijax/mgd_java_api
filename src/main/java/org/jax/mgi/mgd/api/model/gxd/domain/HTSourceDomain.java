package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTSourceDomain extends BaseDomain {
	
	private String abbreviation;
	private String term;
	private int _organism_key;
	



}



//        "_emapa_key": "EMAPS:3535728",
//        "_experiment_key": 9605,
//        "_genotype_key": "MGI:2166310",
//        "_organism_key": 97,
//        "_relevance_key": 20475450,
//        "_sample_key": 637,
//        "_sex_key": 315167,
//        "_stage_key": 28,
//        "age": "postnatal week 14",
//        "agerange": "14",
//        "ageunit": "postnatal week",
//        "notes": []
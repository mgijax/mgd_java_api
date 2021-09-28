package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTSampleDomain extends BaseDomain {
	
	private String name;

	private Integer _sample_key;
	private Integer _experiment_key;
	private Integer _organism_key;
	private Integer _relevance_key;
	private Integer _sex_key;	
	private Integer _stage_key;	
	private Integer _emapa_key;
	private Integer _genotype_key;
	private String age;
	private List<HTNoteDomain> notes;

	private HTGenotypeDomain genotype_object;
	private HTEmapsDomain emaps_object;
}

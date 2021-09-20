package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTSampleDomain extends BaseDomain {
	
	private String name;

	private int _sample_key;
	private int _experiment_key;
	private int _organism_key;
	private int _relevance_key;
	private int _sex_key;	
	private int _emapa_key;
	private int _genotype_key;
	private String age;

	private HTGenotypeDomain genotype_object;

}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTSampleDomain extends BaseDomain {
	
	private Integer _sample_key;
	private Integer _experiment_key;
	private Integer _organism_key;
	private Integer _relevance_key;
	private Integer _sex_key;	
	private Integer _stage_key;	
	private Integer _emapa_key;
	private Integer _genotype_key;
	private String name;	
	private String age;
	private List<HTNoteDomain> notes; 	// old version : used by pwi
	private NoteDomain htNotes;			// new version : at some point, change pwi to use this version
	private HTGenotypeDomain genotype_object;
	private HTEmapsDomain emaps_object;
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTSampleDomain extends BaseDomain {
	
	private String processStatus;
	private Integer _sample_key;
	private Integer _experiment_key;
	private Integer _organism_key;
	private Integer _relevance_key;
	private Integer _sex_key;	
	private Integer _stage_key;			// also in emapas_object; duplicated due to pwi
	private Integer _emapa_key;			// also in emapas_object; duplicated due to pwi
	private Integer _genotype_key;		// also in genotype_object; duplicated due to pwi
	private String name;	
	private String age;
	private String ageunit;
	private String agerange;
	private String notesort;
	private List<HTNoteDomain> notes; 	// old version : used by pwi
	private NoteDomain htNotes;			// new version : at some point, change pwi to use this version
	private HTGenotypeDomain genotype_object;
	private HTEmapsDomain emaps_object;
}

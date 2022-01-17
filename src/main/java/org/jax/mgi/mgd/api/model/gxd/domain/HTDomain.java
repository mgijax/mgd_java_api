package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTDomain extends BaseDomain {

	// A High Throughput Experiement Domain
	
	// basic info
	private int _experiment_key;	
	private String noteCount;	
	private String primaryid;
	private String secondaryid;
	private String name;
	private String description;
	private String notetext;
	private String _note_key;
 	private HTSourceDomain source_object;
	private List<String> experimental_factors;
	private List<String> pubmed_ids;
	private List<String> experiment_types;
	private List<String> provider_contact_names;
	private List<HTExperimentVariableDomain> experiment_variables;
	private List<HTNoteDomain> notes;

	// types and states
	private int _evaluationstate_key;
	private int _experimenttype_key;
	private int _studytype_key;
	private int _curationstate_key;

	// dates
	private String creation_date;
	private String release_date;
	private String lastupdate_date;
	private String evaluated_date;
	private String initial_curated_date;
	private String last_curated_date;

	// users
	private HTUserDomain evaluatedby_object;
	private HTUserDomain initialcuratedby_object;
	private HTUserDomain lastcuratedby_object;

	private List<HTSampleDomain> samples;

	// used in submission of modify/update
	private HTSampleDomain sample_domain; 

	// action flags  
	private int creatingSamples;
	private int deletingSamples;
	private int modifyingSamples;
	private int hasSamples;

}

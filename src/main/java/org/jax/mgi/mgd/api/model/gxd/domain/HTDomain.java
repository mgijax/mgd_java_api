package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTDomain extends BaseDomain {

	// a slim version of AssayDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private int _experiment_key;	
	private String primaryid;
	private String secondaryid;
	private String name;
	private String description;
	private String notetext;

	private int _evaluationstate_key;
	private int _experimenttype_key;
	private int _studytype_key;
	private int _curationstate_key;

	private String creation_date;
	private String release_date;
	private String lastupdate_date;
	private String evaluated_date;
	private String initial_curated_date;
	private String last_curated_date;

	private HTUserDomain evaluatedby_object;
	private HTUserDomain initialcuratedby_object;
	private HTUserDomain lastcuratedby_object;
 


	private List<HTVariableDomain> experiment_variables;
}

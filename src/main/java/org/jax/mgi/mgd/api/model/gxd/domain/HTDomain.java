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
	
	private String _experiment_key;	
	private String primaryid;
	private String secondaryid;
	private String name;
	private String description;
	private String notetext;

	private String _evaluationstate_key;
	private String _experimenttype_key;
	private String _studytype_key;
	private String _curationstate_key;

	private HTUserDomain evaluatedby_object;
	private HTUserDomain initialcuratedby_object;
	private HTUserDomain lastcuratedby_object;
 


	private List<HTVariableDomain> experiment_variables;
}

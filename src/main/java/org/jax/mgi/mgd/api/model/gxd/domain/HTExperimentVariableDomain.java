package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTExperimentVariableDomain extends BaseDomain {
	
	private Integer _experimentvariable_key;
	private String vocabKey;
	private String termKey;
	private int _term_key;
	private String term;
	private String abbreviation;
	private boolean checked;
	
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HTVariableDomain extends BaseDomain {
	
	private String vocabKey;
	private String termKey;
	private int _term_key;
	private String term;
	private String abbreviation;
	private boolean checked;
	
}

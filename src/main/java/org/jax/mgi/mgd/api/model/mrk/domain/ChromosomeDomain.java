package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChromosomeDomain extends BaseDomain {
	
	private String chromosomeKey;
	private String chromosome;
	private String sequenceNum;
	private String organismKey;
	private String commonname;
	private String creation_date;
	private String modification_date;     
}

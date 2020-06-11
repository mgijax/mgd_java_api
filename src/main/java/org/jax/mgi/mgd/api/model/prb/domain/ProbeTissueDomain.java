package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeTissueDomain extends BaseDomain {

	private String tissueKey;
	private String tissue;
	private String standard;
	private String creation_date;
	private String modification_date;
}

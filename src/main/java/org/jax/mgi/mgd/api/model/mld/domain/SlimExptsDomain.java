package org.jax.mgi.mgd.api.model.mld.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimExptsDomain extends BaseDomain {

	private String exptKey;
	private String exptDisplay;
	private String exptType;
	private String chromosome;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;
	private String creation_date;
	private String modification_date;
	private String accID;	
}

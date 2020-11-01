package org.jax.mgi.mgd.api.model.mld.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimExptsDomain extends BaseDomain {

	private String exptKey;
	private String exptDisplay;
	private String exptType;
	private Integer tag;

}

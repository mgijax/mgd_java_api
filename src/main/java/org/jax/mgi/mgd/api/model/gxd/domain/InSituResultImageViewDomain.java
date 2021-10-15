package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InSituResultImageViewDomain extends BaseDomain {

	private String processStatus;
	private String resultImageKey;
	private String resultKey;
	private String imagePaneKey;
	private String figurepaneLabel;
	private String accID;
	private String creation_date;
	private String modification_date;	
}

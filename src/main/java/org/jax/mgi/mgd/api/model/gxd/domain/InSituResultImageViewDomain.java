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
	private String imageKey;
	private String figurepaneLabel;
	private String accID;
	private String pixID;
	private String xDim;
	private String yDim;
	private String x;
	private String y;
	private String width;
	private String height;		
	private String creation_date;
	private String modification_date;	
}

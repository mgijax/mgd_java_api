package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimImagePaneDomain extends BaseDomain {

	private String imagePaneKey;
	private String mgiID;
	private String pixID;
}

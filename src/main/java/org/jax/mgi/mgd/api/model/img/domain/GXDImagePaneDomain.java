package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GXDImagePaneDomain extends BaseDomain {
	// for gxd, list of image pane key, etc.
	// used by getGXDByReference
	
	private String refsKey;
	private String imagePaneKey;
	private String figurepaneLabel;
	private Boolean isUsed = false;

}

package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryImagePaneDomain extends BaseDomain {
	// for gxd, list of image pane key, etc.
	// used by getSummaryByReference
	
	private String refsKey;
	private String imagePaneKey;
	private String jnumID;
	private String imageID;
	private String assayID;
	private String markerID;
	private String assayType;	
	private String figureLabel;
	private String paneLabel;
	private String specimenLabel;
	private String markerSymbol;

}

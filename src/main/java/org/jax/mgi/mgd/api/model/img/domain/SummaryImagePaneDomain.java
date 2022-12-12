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
	private String jnumid;
	private String imageid;
	private String pixid;
	private String assayid;
	private String markerid;
	private String assayType;	
	private String figureLabel;
	private String paneLabel;
	private String specimenLabel;
	private String specimenNote;
	private String markerSymbol;

}

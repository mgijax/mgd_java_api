package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneAssayDomain extends BaseDomain {

	// used by pwi/static/app/edit/imagedetail page
	
	private String imageKey;
	private String imagePaneKey;	
	private String paneLabel;	
	private String assayAccID;
	private String markerKey;	
	private String markerSymbol;
	private String markerAccID;
}

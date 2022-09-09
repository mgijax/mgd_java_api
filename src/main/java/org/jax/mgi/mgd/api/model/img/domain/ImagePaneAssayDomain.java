package org.jax.mgi.mgd.api.model.img.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneAssayDomain extends BaseDomain {

	// used by pwi/static/app/edit/imagedetail page
	
	private String imageKey;
	private String imagePaneKey;	
	private String figureLabel;
	private String paneLabel;
	private List<SlimAssayDomain> assays;
}

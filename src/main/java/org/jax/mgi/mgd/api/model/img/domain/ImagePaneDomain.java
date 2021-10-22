package org.jax.mgi.mgd.api.model.img.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneDomain extends BaseDomain {

	private String processStatus;
	private String imagePaneKey;
	private String imageKey;
	private String paneLabel;
	private String x;
	private String y;
	private String width;
	private String height;	
	private String accID;
	private String creation_date;
	private String modification_date;
	
	private ImageSummaryDomain imageSummary;
	private List<ImagePaneAssocDomain> paneAssocs;
}

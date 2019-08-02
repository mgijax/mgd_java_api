package org.jax.mgi.mgd.api.model.img.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageSubmissionDomain extends BaseDomain {

	private String processStatus;
	private String imageClassKey;
	private String imageClass;	
	private String imageKey;
	private String imageTypeKey;
	private String imageType;
	private String xDim;
	private String yDim;	
	private String figureLabel;
	private String thumbnailFigureLabel;
	private String refsKey;
	private String jnumid;
	private String short_citation;
	private List<AccessionDomain> pixIds;
	private Boolean hasPixId;
	private String pixStatus;
	private List<ImageSubmissionFileDomain> submissionFiles;
	
}

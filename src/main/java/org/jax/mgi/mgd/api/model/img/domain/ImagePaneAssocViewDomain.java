package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneAssocViewDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String imagePaneKey;
	private String mgiTypeKey;
	private String objectKey;
	private String isPrimary;
	private String figureLabel;
	private String term;
	private String mgiID;
	private String pixID;
}

package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneAssocDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String imagePaneKey;
	private String mgiTypeKey;
	private String objectKey;
	private String isPrimary;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
//	if looking for the ImagePaneAssoc/Allele, see AlleleService/getAlleleByImage()
	
}

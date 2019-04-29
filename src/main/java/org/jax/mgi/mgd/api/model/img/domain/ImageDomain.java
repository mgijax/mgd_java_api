package org.jax.mgi.mgd.api.model.img.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageDomain extends BaseDomain {

	// do not use 'processStatus' because this is a master domain
	// and only 1 master domain record is processed by the create/update endpoint
	
	private String imageKey;
	private String imageClassKey;
	private String imageClass;
	private String imageTypeKey;
	private String imageType;
	private String xDim;
	private String yDim;
	private String figureLabel;
	private String refsKey;
	private String jnumid;
	private String short_citation;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private NoteDomain captionNote;
	private NoteDomain copyrightNote;
	private NoteDomain privateCuratorialNote;
	private NoteDomain externalLinkNote;
	
	private List<AccessionDomain> mgiAccessionIds;
	private List<AccessionDomain> editAccessionIds;
	
	// exists in domain, but not populated by marker translator
	// instead will be populated by call to specific domain service	
	private List<AccessionDomain> nonEditAccessionIds;

	private List<ImagePaneDomain> imagePanes;
	private ImageDomain thumbnailImage;
	
}

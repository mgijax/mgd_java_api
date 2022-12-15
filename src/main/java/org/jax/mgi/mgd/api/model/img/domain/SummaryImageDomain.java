package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryImageDomain extends BaseDomain {
	// for list of image key, etc.
	// used by getImageByAllele

	private String alleleKey;
	private String alleleid;
	private String alleleSymbol;
	private String imageKey;
	private String imageid;	
	private String pixid;
	private String figureLabel;
	private String xdim;
	private String ydim;
	private String caption;
	private String copyright;
	private String alleleComposition;
	private String geneticBackgroud;

}

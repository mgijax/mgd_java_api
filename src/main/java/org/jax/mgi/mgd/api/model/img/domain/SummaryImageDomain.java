package org.jax.mgi.mgd.api.model.img.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryImageDomain extends BaseDomain {
	// for gxd, list of image key, etc.
	// used by getImageByAllele
	
	private String alleleKey;
	private String alleleSymbol;
	private String alleleID;

	List<ImageDomain> images;
	List<ImagePaneAlleleViewDomain> alleleAssocs;
	List<ImagePaneGenotypeViewDomain> genotypeAssocs;
}

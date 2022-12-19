package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneGenotypeViewDomain extends BaseDomain {

	private String assocKey;
	private String alleleKey;
	private String imagePaneKey;
	private String strain;
	private String alleleComposition;
}

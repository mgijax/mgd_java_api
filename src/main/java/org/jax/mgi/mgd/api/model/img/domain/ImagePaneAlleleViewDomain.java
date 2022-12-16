package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImagePaneAlleleViewDomain extends BaseDomain {

	private String assocKey;
	private String alleleKey;
	private String figureLabel;
	private String symbol;
	private String alleleid;

}

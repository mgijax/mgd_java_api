package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimAlleleCellLineDerivationDomain extends BaseDomain {

	private String alleleTypeKey;
	private String vectorKey;
	private String parentCellLineKey;
	private String creatorKey;
	private String strainKey;
	private String cellLineTypeKey;
}

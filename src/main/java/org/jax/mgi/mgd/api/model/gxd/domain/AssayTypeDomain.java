package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayTypeDomain extends BaseDomain {

	private String vocabKey = "158";
	private int isRNAAssay;
	private int isGelAssay;
	private int sequenceNum;
	private String name = "GXD Assay Type";
	private List<TermDomain> terms;
}

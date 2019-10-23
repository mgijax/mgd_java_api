package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeMPDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private List<AccessionDomain> mgiAccessionIds;
	private List<AnnotationDomain> mpAnnots;
	private List<AnnotationHeaderDomain> mpHeaders;
	private Boolean allowEditTerm = false;	
}

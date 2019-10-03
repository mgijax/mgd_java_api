package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeMPDomain extends BaseDomain {

	private String processStatus;
	private String genotypeKey;
	private String genotypeDisplay;
	
	private List<AccessionDomain> mgiAccessionIds;
	// sc 9/18 updated this from and GenotypeMPAnnotationDomain as that domain had everything
	// the AnnotationDomain has
	private List<AnnotationDomain> mpAnnots;
	private List<AnnotationHeaderDomain> mpHeaders;
	
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeMPDomain extends BaseDomain {

	private String genotypeKey;
	private List<AnnotationDomain> mpAnnots;
	private List<AnnotationHeaderDomain> mpHeaders;
	
}

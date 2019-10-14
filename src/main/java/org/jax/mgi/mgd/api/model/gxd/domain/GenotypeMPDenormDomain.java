package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationMPDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeMPDenormDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private String accid;
	private List<AnnotationMPDomain> mpAnnots;
	//private List<AnnotationHeaderDomain> mpHeaders;
	
}

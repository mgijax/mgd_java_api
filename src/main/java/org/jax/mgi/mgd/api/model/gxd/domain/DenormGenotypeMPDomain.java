package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DenormGenotypeMPDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private String accid;
	private List<DenormAnnotationDomain> mpAnnots;
	private List<AnnotationHeaderDomain> mpHeaders;
	private Boolean allowEditTerm = false;	
}

package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DenormGenotypeAnnotDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private String accID;
	private List<DenormAnnotationDomain> annots;
	private List<AnnotationHeaderDomain> headers;
	private List<GenotypeAnnotHeaderViewDomain> headersByAnnot;	
	private Boolean allowEditTerm = false;	
}

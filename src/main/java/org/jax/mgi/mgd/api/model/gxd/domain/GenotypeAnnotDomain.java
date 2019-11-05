package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeAnnotDomain extends BaseDomain {

	private String genotypeKey;
	private String genotypeDisplay;
	private List<AccessionDomain> mgiAccessionIds;
	private List<AnnotationDomain> annots;
	private List<AnnotationHeaderDomain> headers;
	private Boolean allowEditTerm = false;	
	
	public void setAnnot(AnnotationDomain domain) {
		annots.add(domain);
	}
}

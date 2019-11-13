package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleAnnotDomain extends BaseDomain {

	private String alleleKey;
	private List<AccessionDomain> mgiAccessionIds;
	private List<AnnotationDomain> annots;
	private Boolean allowEditTerm = false;	
	
	
}

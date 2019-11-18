package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DenormAlleleAnnotDomain extends BaseDomain {

	private String alleleKey;
	private String alleleDisplay;
	private String accid;
	private List<DenormAnnotationDomain> annots;
	private Boolean allowEditTerm = false;	
}

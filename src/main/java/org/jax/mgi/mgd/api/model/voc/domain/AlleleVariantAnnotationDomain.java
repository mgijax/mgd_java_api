package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Allele Variant Annotation Domain")
public class AlleleVariantAnnotationDomain extends BaseDomain {

	// note:
	// Allele Variant Annotations do not create Evidence records
	
	private String processStatus;
	private String annotKey;
	private String annotTypeKey;	
    private String termKey;
    private String term;
    private List<SlimAccessionDomain> alleleVariantSOIds;	
}

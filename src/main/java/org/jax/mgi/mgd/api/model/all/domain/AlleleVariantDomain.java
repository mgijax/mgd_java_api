package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantAnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleVariantDomain extends BaseDomain {
	//processStatus not needed as this is a 'master' table
	private String variantKey;	
	private String isReviewed;
	private String description;
	private String chromosome;
	private String strand;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
		
	private SlimAlleleDomain allele;
	private SlimProbeStrainDomain strain;
    private List<AlleleVariantAnnotationDomain> variantTypes;
    private List<AlleleVariantAnnotationDomain> variantEffects;
	private List<VariantSequenceDomain> variantSequences;
	private NoteDomain curatorNote;	
	private NoteDomain publicNote;
	private List<MGIReferenceAssocDomain> refAssocs;

	private AlleleVariantDomain sourceVariant;
	
} 

package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenotypeDomain extends BaseDomain {

	private String genotypeKey;
	private String strainKey;
	private String strain;
	private String isConditional;
	private String existsAsKey;
	private String existsAs;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private String useAllelePairDefaultOrder;
	
	private NoteDomain alleleDetailNote;
	private NoteDomain generalNote;
	private NoteDomain privateCuratorialNote;
	
	private List<AccessionDomain> mgiAccessionIds;
	private List<AllelePairDomain> allelePairs;
	private List<ImagePaneAssocViewDomain> imagePaneAssocs;

	private List<AnnotationDomain> mpAnnots;
	private List<AnnotationDomain> doAnnots;
	
}

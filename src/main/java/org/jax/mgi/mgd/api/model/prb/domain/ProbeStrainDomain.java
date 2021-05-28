package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeStrainDomain extends BaseDomain {

	private String strainKey;
	private String strain;
	private String standard;
	private String isPrivate;
	private String geneticBackground;
	private String speciesKey;
	private String species;
	private String strainTypeKey;
	private String strainType;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;
	
	private NoteDomain strainOriginNote;
	private NoteDomain impcNote;
	private NoteDomain nomenNote;
	private NoteDomain mclNote;
	private List<AccessionDomain> mgiAccessionIds;	
	private List<AccessionDomain> otherAccIds;
	private List<AnnotationDomain> attributes;
	private List<AnnotationDomain> needsReview;
	private List<ProbeStrainMarkerDomain> markers;
	private List<ProbeStrainGenotypeDomain> genotypes;
	private List<MGISynonymDomain> synonyms;
	private List<MGIReferenceAssocDomain> refAssocs;
	
}

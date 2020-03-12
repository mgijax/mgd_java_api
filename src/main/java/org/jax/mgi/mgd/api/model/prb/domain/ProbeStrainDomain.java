package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
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
	private NoteDomain impcColonyNote;
	private NoteDomain nomenclatureNote;
	private NoteDomain mutantCellLineNote;
	private List<AnnotationDomain> attributes;
	private List<ProbeStrainMarkerDomain> markers;
	private List<ProbeStrainGenotypeDomain> genotypes;
	private List<MGISynonymDomain> synonyms;
	
}

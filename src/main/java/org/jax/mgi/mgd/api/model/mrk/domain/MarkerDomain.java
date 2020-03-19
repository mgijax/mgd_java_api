package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerDomain extends BaseDomain {

	// do not use 'processStatus' because this is a master domain
	// and only 1 master domain record is processed by the create/update endpoint
	
	private String markerKey;
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
	private String organismKey;
	private String organism;
	private String organismLatin;
	private String markerStatusKey;
	private String markerStatus;
	private String markerTypeKey;
	private String markerType;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private NoteDomain editorNote;
	private NoteDomain sequenceNote;
	private NoteDomain revisionNote;
	private NoteDomain strainNote;
	private NoteDomain locationNote;
	private MarkerNoteDomain detailClipNote;

	private List<AccessionDomain> mgiAccessionIds;
	private List<MarkerHistoryDomain> history;
	private List<MGISynonymDomain> synonyms;
	
	private List<AccessionDomain> editAccessionIds;
	private List<MGIReferenceAssocDomain> refAssocs;
	
	// display only, see individual object/service to methods that set these values
	private List<MarkerFeatureTypeDomain> featureTypes;
	private List<SlimMarkerDomain> tssToGene;
	private List<SlimMarkerDomain> aliases;
	 
	// exists in domain, but not populated by marker translator
	// instead will be populated by call to specific domain service	
	private List<AccessionDomain> nonEditAccessionIds;

}

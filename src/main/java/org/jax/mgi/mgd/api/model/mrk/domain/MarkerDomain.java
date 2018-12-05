package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerDomain extends BaseDomain {

	//@ApiModelProperty("primary key")
	private String markerKey;
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
	private String organismKey;
	private String organism;
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
	
	private List<AccessionDomain> mgiAccessionIds;
	private List<MarkerHistoryDomain> history;
	private List<MGISynonymDomain> synonyms;
	
	// exists in domain, but not populated by marker translator
	// instead will be populated by call to specific domain service
	//
	private List<AccessionDomain> nucleotideAccessionIds;
	private List<AccessionDomain> otherAccessionIds;
	private List<MGIReferenceAssocDomain> refAssocs;
	
	//private String mcvTerm;
	//private List<String> geneToTssRelationships;
	//private List<String> tssToGeneRelationships;
        
}

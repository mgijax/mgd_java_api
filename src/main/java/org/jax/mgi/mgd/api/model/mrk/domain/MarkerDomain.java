package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerDomain extends BaseDomain {

	//@ApiModelProperty("primary key")
	private Integer markerKey;
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
	private Date creation_date;
	private Date modification_date;
	
	private NoteDomain editorNote;
	private NoteDomain sequenceNote;
	private NoteDomain revisionNote;
	private NoteDomain strainNote;
	private NoteDomain locationNote;
	
	private List<AccessionDomain> mgiAccessionIds;
	
	//private String markerNote; 
	//private String locationChromosome;
	//private Integer locationStartCoordinate;
	//private Integer locationEndCoordinate;
	//private String locationStrand;
	//private String locationMapUnits;
	//private String locationProvider;
	//private String locationVersion;
	//private String locationText;
	//private String mcvTerm;
	//private String mgiAccessionId;
	
	//private List<String> synonyms;
	//private List<String> geneToTssRelationships;
	//private List<String> tssToGeneRelationships;
	//private List<String> secondaryMgiIds;
    //private List<SequenceMarkerCacheDomain> biotypes;
        
}

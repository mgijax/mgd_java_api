package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Model Object")
public class MarkerDomain extends BaseDomain {

	@ApiModelProperty("Marker primary key")
	private Integer markerKey;
	@ApiModelProperty("Marker Symbol")
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
	private Integer organismKey;
	private String organism;
	private Integer markerStatusKey;
	private String markerStatus;
	private Integer markerTypeKey;
	private String markerType;
	private Integer createdByKey;
	private String createdBy;
	private Integer modifiedByKey;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	
	private String editorNote;
	private String sequenceNote;
	private String revisionNote;
	private String strainNote;
	private String locationNote;

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

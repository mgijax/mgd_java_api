package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceMarkerCacheDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Model Object")
public class MarkerDomain extends BaseDomain {

	@ApiModelProperty("Marker Database Key")
	private Integer markerKey;
	@ApiModelProperty("Marker Symbol")
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String organism;
	private String markerStatus;
	private String markerType;
	private String markerNote;
	private String locationChromosome;
	private Integer locationStartCoordinate;
	private Integer locationEndCoordinate;
	private String locationStrand;
	private String locationMapUnits;
	private String locationProvider;
	private String locationVersion;
	private String locationText;
	private String locationNote;
	private String mcvTerm;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	private String mgiAccessionId;
    
	private List<String> synonyms;
	private List<String> geneToTssRelationships;
	private List<String> tssToGeneRelationships;
	private List<String> secondaryMgiIds;
    private List<String> references;
    private List<SequenceMarkerCacheDomain> biotypes;

    private Boolean hasAlleles = false;
    private Boolean hasAntibodies = false;
    private Boolean hasAssays = false;
    private Boolean hasAssayResults = false;
    private Boolean hasExperiments = false;
    private Boolean hasIndexes = false;
    private Boolean hasProbes = false;
    private Boolean hasReferences = false;
    private Boolean hasSequences = false;
	
}

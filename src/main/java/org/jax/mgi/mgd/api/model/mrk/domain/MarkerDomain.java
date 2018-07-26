package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;

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
	private String locationNote;
	private String mcvTerm;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	private String mgiAccessionId;
    
	private List<String> secondaryMgiIds;
	private List<AlleleDomain> alleles;
	private List<AntibodyDomain> antibodies;
	private List<AssayDomain> assays;
	private List<ExperimentDomain> expts;
    private List<IndexDomain> indexes;
    private List<MGISynonymDomain> synonyms;
	private List<ProbeDomain> probes;
    private List<String> references;
    private List<SequenceDomain> sequences;
    
}

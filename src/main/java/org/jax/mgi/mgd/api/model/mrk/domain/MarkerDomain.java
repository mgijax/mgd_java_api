package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;

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
	private String createdBy;
	private String modifiedBy;
	private String mgiAccessionId;

	private List<AlleleDomain> alleles;
	private List<AntibodyDomain> antibodies;
	private List<AssayDomain> assays;
	private List<ExperimentDomain> expts;
    private List<IndexDomain> indexes;
	private List<ProbeDomain> probes;
    private List<ReferenceDomain> references;
    private List<SequenceDomain> sequences;

}

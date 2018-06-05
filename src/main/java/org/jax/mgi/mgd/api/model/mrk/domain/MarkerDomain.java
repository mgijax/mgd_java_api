package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;

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
	private String createdBy;
	private String modifiedBy;
	private String mgiAccessionId;

	private List<AlleleDomain> alleles;
	private List<AssayDomain> assays;
	private List<ProbeMarkerDomain> probeMarkers;

	//private List<String> synonyms;
	//private int allele_count;

}

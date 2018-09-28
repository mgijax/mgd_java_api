package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceMarkerCacheDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker EI Summary Model Object")
public class MarkerEiSummaryDomain extends BaseDomain {

	private Map<String, String> summaryMarkers;        
}

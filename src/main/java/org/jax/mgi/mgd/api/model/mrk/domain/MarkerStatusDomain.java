package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Status Model Object")
public class MarkerStatusDomain extends BaseDomain {

	//@ApiModelProperty("Marker primary key")
	private Integer markerStatusKey;
	private String markerStatus;
	private Date creation_date;
	private Date modification_date;     
}

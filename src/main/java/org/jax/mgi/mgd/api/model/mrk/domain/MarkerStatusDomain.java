package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Status Domain Object")
public class MarkerStatusDomain extends BaseDomain {
	
	private Integer markerStatusKey;
	private String markerStatus;
	private Date creation_date;
	private Date modification_date;     
}

package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Type Domain Object")
public class MarkerTypeDomain extends BaseDomain {
	
	private Integer markerTypeKey;
	private String markerType;
	private Date creation_date;
	private Date modification_date;     
}

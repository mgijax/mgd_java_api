package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Marker Model Object")
@Table(name="mrk_marker")
public class Marker extends Base {

	@Id
	@Column(name="_marker_key")
	public Long markerKey;
	
	public String symbol;
	public String name;
	public String chromosome;
	
	
}

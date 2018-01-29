package org.jax.mgi.mgd.api.model.wks.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "WKS Rosetta Object")
@Table(name="wks_rosetta")
public class Rosetta extends EntityBase {
	@Id
	private Integer _rosetta_key;
	private String wks_markerSymbol;
	private String wks_markerURL;
	private Date creation_date;
	private Date modification_date;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key")
	private Marker marker;

}

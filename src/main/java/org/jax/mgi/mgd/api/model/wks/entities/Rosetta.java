package org.jax.mgi.mgd.api.model.wks.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "WKS Rosetta Object")
@Table(name="wks_rosetta")
public class Rosetta extends BaseEntity {
	@Id
	private int _rosetta_key;
	private String wks_markerSymbol;
	private String wks_markerURL;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_marker_key")
	private Marker marker;

}

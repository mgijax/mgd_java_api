package org.jax.mgi.mgd.api.model.wks.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

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
	private Integer sequenceNum;
	private String wks_markerSymbol;
	private String wks_markerURL;
	private Date creation_date;
	private Date modification_date;

}

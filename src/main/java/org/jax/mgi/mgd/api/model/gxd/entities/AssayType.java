package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "Assay Type Model Object")
@Table(name="gxd_assaytype")
public class AssayType extends EntityBase {

	@Id
	private Integer _assaytype_key;
	private String assayType;
	private Integer isRNNAAssay;
	private Integer isGelAssay;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}

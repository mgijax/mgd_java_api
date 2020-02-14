package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Assay Type Model Object")
@Table(name="gxd_assaytype")
public class AssayType extends BaseEntity {

	@Id
	private Integer _assaytype_key;
	private String assayType;
	private Integer isRNAAssay;
	private Integer isGelAssay;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}

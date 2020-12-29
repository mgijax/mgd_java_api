package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Assay Type Model Object")
@Table(name="gxd_assaytype")
public class AssayType extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_assaytype_generator")
	@SequenceGenerator(name="gxd_assaytype_generator", sequenceName = "gxd_assaytype_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _assaytype_key;
	private String assayType;
	private Integer isRNAAssay;
	private Integer isGelAssay;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}

package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antibody Class Model Object")
@Table(name="gxd_antibodyclass")
public class AntibodyClass extends BaseEntity {

	@Id
	private Integer _antibodyclass_key;
	@Column(name="class")           // java reserved word
	private String antibodyClass;	
	private Date creation_date;
	private Date modification_date;
}

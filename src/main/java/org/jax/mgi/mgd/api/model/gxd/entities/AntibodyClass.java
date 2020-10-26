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
@ApiModel(value = "Antibody Class Model Object")
@Table(name="gxd_antibodyclass")
public class AntibodyClass extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antibodyclass_generator")
	@SequenceGenerator(name="gxd_antibodyclass_generator", sequenceName = "gxd_antibodyclass_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _antibodyclass_key;
	private String antibodyClass;	
	private Date creation_date;
	private Date modification_date;
}

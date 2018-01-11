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
@ApiModel(value = "TheilerStage Model Object")
@Table(name="gxd_theilerstage")
public class TheilerStage extends EntityBase {

	@Id
	private Integer _stage_key;
	private Integer stage;
	private String description;
	private Integer dpcMin;
	private Integer dpcMax;
	private Date creation_date;
	private Date modification_date;
}

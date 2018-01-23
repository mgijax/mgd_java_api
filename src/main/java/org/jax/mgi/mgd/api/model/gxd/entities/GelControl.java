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
@ApiModel(value = "GelControl Model Object")
@Table(name="gxd_gelcontrol")
public class GelControl extends EntityBase {

	@Id
	private Integer _gelcontrol_key;
	private String gelLaneContent;
	private Date creation_date;
	private Date modification_date;
}

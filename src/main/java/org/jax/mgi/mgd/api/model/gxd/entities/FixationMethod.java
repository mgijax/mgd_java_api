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
@ApiModel(value = "FixationMethod Model Object")
@Table(name="gxd_fixationmethod")
public class FixationMethod extends EntityBase {

	@Id
	private Integer _fixation_key;
	private String fixation;
	private Date creation_date;
	private Date modification_date;
}

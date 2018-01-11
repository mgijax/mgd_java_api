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
@ApiModel(value = "Antibody Model Object")
@Table(name="gxd_antibody")
public class Antibody extends EntityBase {

	@Id
	private Integer _antibody_key;
	private String antibodyName;
	private String antibodyNote;
	private Date creation_date;
	private Date modification_date;
}

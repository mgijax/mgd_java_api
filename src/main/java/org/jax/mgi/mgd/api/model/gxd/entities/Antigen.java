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
@ApiModel(value = "Antigen Model Object")
@Table(name="gxd_antigen")
public class Antigen extends EntityBase {

	@Id
	private Integer _antigen_key;
	private String antigenName;
	private String regionCovered;
	private String antigenNote;
	private Date creation_date;
	private Date modification_date;
}

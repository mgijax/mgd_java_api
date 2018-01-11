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
@ApiModel(value = "Antibody Alias Model Object")
@Table(name="gxd_antibodyalias")
public class AntibodyAlias extends EntityBase {

	@Id
	private Integer _antibodyalias_key;
	private String alias;
	private Date creation_date;
	private Date modification_date;
}

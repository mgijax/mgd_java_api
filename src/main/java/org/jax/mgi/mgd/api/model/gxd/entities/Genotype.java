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
@ApiModel(value = "Genotype Model Object")
@Table(name="gxd_genotype")
public class Genotype extends EntityBase {

	@Id
	private Integer _genotype_key;
	private Integer isConditional;
	private String note;
	private Date creation_date;
	private Date modification_date;
}

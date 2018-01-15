package org.jax.mgi.mgd.api.model.all.entities;

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
@ApiModel(value = "CellLine Derivation Model Object")
@Table(name="all_cellline_derivation")
public class CellLineDerivation extends EntityBase {

	@Id
	private Integer _derivation_key;
	private String name;
	private String description;
	private Date creation_date;
	private Date modification_date;
}

package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MLD Assay Types Object")
@Table(name="mld_assay_types")
public class Assay_Types extends EntityBase {
	@Id
	private Integer _assay_Type_key;
	private String description;
		private Date creation_date;
	private Date modification_date;

}

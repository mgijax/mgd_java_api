package org.jax.mgi.mgd.api.model.crs.entities;

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
@ApiModel(value = "Cross Matrix Model Object")
@Table(name="crs_matrix")
public class Matrix extends EntityBase {

	@Id
	private Integer _cross_key;
	private String otherSymbol;
	private String chromosome;
	private Integer rowNumber;
	private String notes;
	private Date creation_date;
	private Date modification_date;
	
}

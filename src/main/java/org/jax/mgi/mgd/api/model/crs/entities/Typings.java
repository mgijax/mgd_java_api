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
@ApiModel(value = "Cross Typings Model Object")
@Table(name="crs_typings")
public class Typings extends EntityBase {

	@Id
	private Integer _cross_key;
	private Integer rowNumber;
	private Integer colNumber;
	private String data;
	private Date creation_date;
	private Date modification_date;
	
}

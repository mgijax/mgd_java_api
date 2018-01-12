package org.jax.mgi.mgd.api.model.mgi.entities;

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
@ApiModel(value = "Property Type Object")
@Table(name="mgi_propertytype")
public class PropertyType extends EntityBase {
	@Id
	private Integer _propertyType_key;
	private String propertyType;
	private Date creation_date;
	private Date modification_date;

}

package org.jax.mgi.mgd.api.model.mgi.entities;

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
@ApiModel(value = "Relationship Category Object")
@Table(name="mgi_relationship_category")
public class Relationship_Category extends EntityBase {
	@Id
	private Integer _category_key;
	private String name;
	private Date creation_date;
	private Date modification_date;

}

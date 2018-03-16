package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Types Model Object")
@Table(name="mrk_types")
public class MarkerType extends BaseEntity {

	@Id
	private Integer _marker_type_key;
	private String name;
	private Date creation_date;
	private Date modification_date;
}

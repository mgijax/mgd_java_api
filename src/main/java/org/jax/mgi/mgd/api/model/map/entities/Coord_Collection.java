package org.jax.mgi.mgd.api.model.map.entities;

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
@ApiModel(value = "MAP Coordinate Collection Model Object")
@Table(name="map_coord_collection")
public class Coord_Collection extends EntityBase {

	@Id
	private Integer _collection_key;
	private String name;
	private String abbreviation;
	private Date creation_date;
	private Date modification_date;
	
}

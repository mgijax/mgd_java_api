package org.jax.mgi.mgd.api.model.map.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MAP Coordinate Feature Model Object")
@Table(name="map_coord_feature")
public class CoordinateFeature extends BaseEntity {

	@Id
	private int _feature_key;
	private int startCoordinate;
	private int endCoordinate;
	private String strand;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_map_key")
	private Coordinate map;
	
	@OneToOne
	@JoinColumn(name="_mgiType_key")
	private MGIType mgiType;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}

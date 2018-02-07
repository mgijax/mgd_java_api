package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Reference Model Object")
@Table(name="crs_references")
public class CrossReference extends EntityBase {

	@EmbeddedId
	private CrossReferenceKey key;
	private Date creation_date;
	private Date modification_date;

	// runtime error: column : _marker_key (should be mapped with insert="false" update = "false"
	//@OneToOne
	//@JoinColumn(name="_marker_key")
	//private Marker marker;

	//@OneToOne
	//@JoinColumn(name="_refs_key")
	//private Reference reference;

}

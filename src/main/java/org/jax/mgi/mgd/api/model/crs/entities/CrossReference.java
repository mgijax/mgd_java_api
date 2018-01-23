package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

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

	// runtime error: column : _marker_key (should be mapped with insert=”false” update = “false”
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	//private Marker marker;
	
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	//private Reference reference;
	
	@Getter @Setter
	@Embeddable
	public class CrossReferenceKey implements Serializable {
		private Integer _cross_key;
		private Integer _marker_key;
		private Integer _refs_key;
	}
}

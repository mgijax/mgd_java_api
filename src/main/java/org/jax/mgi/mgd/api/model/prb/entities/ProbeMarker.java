package org.jax.mgi.mgd.api.model.prb.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBMarker Model Object")
@Table(name="prb_marker")
public class ProbeMarker extends EntityBase {

	// This only needs to be done if there is no other primary key for the table
	@EmbeddedId
	private ProbeMarkerKey key;

	private String relationship;
	private Date creation_date;
	private Date modification_date;


	@Getter @Setter
	@Embeddable
	public class ProbeMarkerKey implements Serializable {
		private Integer _probe_key;
		private Integer _marker_key;
	}
}

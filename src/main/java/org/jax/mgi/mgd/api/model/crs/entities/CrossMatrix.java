package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Matrix Model Object")
@Table(name="crs_matrix")
public class CrossMatrix extends BaseEntity {

	@EmbeddedId
	private CrossMatrixKey key;
	private String otherSymbol;
	private String chromosome;
	private String notes;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
}

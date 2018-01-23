package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Matrix Model Object")
@Table(name="crs_matrix")
public class Matrix extends EntityBase {

	@EmbeddedId
	private MatrixKey key;
	private String otherSymbol;
	private String chromosome;
	private String notes;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Marker marker;
	
	@Getter @Setter
	@Embeddable
	public class MatrixKey implements Serializable {
		private Integer _cross_key;
		private Integer rowNumber;
	}
}

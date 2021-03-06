package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
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
@ApiModel(value = "RI2Point Model Object")
@Table(name="mld_ri2point")
public class MLDRI2Point extends BaseEntity {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private Integer numRecombinants;
	private Integer numTotal;
	private String RI_Lines;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_marker_key_1", referencedColumnName="_marker_key")
	private Marker marker1;
	
	@OneToOne
	@JoinColumn(name="_marker_key_2", referencedColumnName="_marker_key")
	private Marker marker2;
	
}

package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@ApiModel(value = "MC2point Model Object")
@Table(name="mld_mc2point")
public class MC2point extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private Integer numRecombinants;
	private Integer numParentals;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key_1", referencedColumnName="_marker_key_1")
	private Marker marker1;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key_2", referencedColumnName="_marker_key_2")
	private Marker marker2;
	
}

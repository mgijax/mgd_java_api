package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GelBand Model Object")
@Table(name="gxd_gelband")
public class GelBand extends EntityBase {

	@Id
	private Integer _gelband_key;
	private String bandNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_gellane_key")
	private GelLane gelLane;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_gelrow_key")
	private GelRow gelRow;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strength_key")
	private Strength strength;
}
